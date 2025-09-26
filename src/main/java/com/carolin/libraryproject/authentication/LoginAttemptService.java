package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.user.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


// Håller koll på misslyckade inloggnings försök.
@Component
public class LoginAttemptService {

    // Temporär låsning av kontot i en minut, efter 5 försök på en minut
    private static final int MAX_ATTEMPTS_PER_MINUTE = 5;
    private static final Duration BLOCK_TIME_WINDOW = Duration.ofMinutes(1);

    // Efter upprepade misslyckade inloggnings försök låses kontot helt efter 20 försök på en dag.
    private static final int MAX_ATTEMPTS_TOTAL = 20;
    private static final Duration TOTAL_WINDOW = Duration.ofDays(1);

    private Map<String, List<LocalDateTime>> attempts = new ConcurrentHashMap<>();

    private final UserRepository userRepository;


    public LoginAttemptService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }



    //
    public void loginFailed(String email) {

        // Hämtar tid
        LocalDateTime now = LocalDateTime.now();

        // Hämtar listan med misslyckade inloggnings försök
        List<LocalDateTime> userAttemts = attempts.getOrDefault(email, new ArrayList<>());

        //Lägger till nytt misslyckat försök
        userAttemts.add(now);

        // Tar bort tidigare misslyckade försök som är äldre än en minut
        userAttemts.removeIf(time -> time.isBefore(now.minus(BLOCK_TIME_WINDOW)));

        // Hämtar listan med försök och om användaren inte finns skapas en ny lista.
        attempts.put(email, userAttemts);

        // Om antalet inloggnings försök är 5 loggas det.
        if (userAttemts.size() >= MAX_ATTEMPTS_PER_MINUTE) {
            System.out.println("Login attempts exceeded");
        }

        // räknar totala
        long recentAttempts = userAttemts.stream()
                .filter(time -> time.isAfter(now.minus(TOTAL_WINDOW)))
                        .count();

        // Kontroll om inloggnings försöket misslyckas under en dag låses kontot
        if (recentAttempts >= MAX_ATTEMPTS_TOTAL) {
            lockUserAccount(email);
        }
    }



    // Kontrollerar om en användare är tillfälligt blockerad
    public boolean isTemporarilyBlocked(String email) {
        List<LocalDateTime> userAttempts = attempts.getOrDefault(email, Collections.emptyList());
        if (userAttempts.isEmpty()) return false;
        LocalDateTime now = LocalDateTime.now();
        long recent = userAttempts.stream()
                .filter(time -> time.isAfter(now.minus(BLOCK_TIME_WINDOW)))
                .count();
        return recent >= MAX_ATTEMPTS_PER_MINUTE;
    }


// Kontrollerar om en användare är helt blockerad
    public boolean isBlocked(String email) {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> userAttempts = attempts.getOrDefault(email, new ArrayList<>());
        userAttempts.removeIf(time -> time.isBefore(now.minus(BLOCK_TIME_WINDOW)));
        return userAttempts.size() >= MAX_ATTEMPTS_PER_MINUTE;
    }



// Om en användare lyckas logga in tas misslyckade försök bort från listan
    public void loginSuccess(String email) {

        attempts.remove(email);
    }


    // Låser användares konto
    public void lockUserAccount(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
    }

}
