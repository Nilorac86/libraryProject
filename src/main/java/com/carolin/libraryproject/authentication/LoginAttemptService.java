package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.user.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {



    private static final int MAX_ATTEMPTS_PER_MINUTE = 5;
    private static final Duration BLOCK_TIME_WINDOW = Duration.ofMinutes(1);

    private static final int MAX_ATTEMPTS_TOTAL = 20;
    private static final Duration TOTAL_WINDOW = Duration.ofDays(1);

    private Map<String, List<LocalDateTime>> attempts = new ConcurrentHashMap<>();

    private final UserRepository userRepository;


    public LoginAttemptService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public void loginFaild(String email) {
        LocalDateTime now = LocalDateTime.now();

        List<LocalDateTime> userAttemts = attempts.getOrDefault(email, new ArrayList<>());

        userAttemts.add(now);
        userAttemts.removeIf(time -> time.isBefore(now.minus(BLOCK_TIME_WINDOW)));


        attempts.put(email, userAttemts);

        if (userAttemts.size() >= MAX_ATTEMPTS_PER_MINUTE) {
            System.out.println("Login attempts exceeded");
        }

        long recentAttempts = userAttemts.stream()
                .filter(time -> time.isAfter(now.minus(TOTAL_WINDOW)))
                        .count();

        if (recentAttempts >= MAX_ATTEMPTS_TOTAL) {
            lockUserAccount(email);
        }
    }


///  ??????
    public boolean isTemporarilyBlocked(String email) {
        List<LocalDateTime> userAttempts = attempts.getOrDefault(email, Collections.emptyList());
        if (userAttempts.isEmpty()) return false;
        LocalDateTime now = LocalDateTime.now();
        long recent = userAttempts.stream()
                .filter(time -> time.isAfter(now.minus(BLOCK_TIME_WINDOW)))
                .count();
        return recent >= MAX_ATTEMPTS_PER_MINUTE;
    }



    public boolean isBlocked(String email) {
        LocalDateTime now = LocalDateTime.now();
        List<LocalDateTime> userAttempts = attempts.getOrDefault(email, new ArrayList<>());
        userAttempts.removeIf(time -> time.isBefore(now.minus(BLOCK_TIME_WINDOW)));
        return userAttempts.size() >= MAX_ATTEMPTS_PER_MINUTE;
    }




    public void loginSuccess(String email) {
        attempts.remove(email);
    }



    public void lockUserAccount(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
    }

}
