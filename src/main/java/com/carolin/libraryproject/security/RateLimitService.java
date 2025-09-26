package com.carolin.libraryproject.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitService {

    private Map<String, List<Long>> requestTimes= new ConcurrentHashMap<>();

    // Max 10 försök på en minut
    private final int MAX_REQUESTS = 10;
    private final int TIME_WINDOW = 60000;


    // Kontrollerar om
    public boolean isAllowed(String clientIp) {
        // Aktuell tid
        long currentTime = System.currentTimeMillis();

        // Hämtar listan med tidigare request eller skapar en ny lista om ingen finns
        List<Long> times = requestTimes.getOrDefault(clientIp,  new ArrayList<>());

        // Tar bort requests som är äldre än en minut
        times.removeIf(time -> currentTime - time > TIME_WINDOW);

        // Om request inom tidsramen är för många
        if (times.size() >= MAX_REQUESTS) {
            return false;
        }

        // Lägger till den aktuella tiden och sparar i listan
        times.add( currentTime);
        requestTimes.put(clientIp, times);

        return true;
    }


    // Hämtar användarens ipAdress
    public static String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
