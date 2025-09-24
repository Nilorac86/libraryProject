package com.carolin.libraryproject.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

    public boolean isAllowed(String clientIp) {
        long currentTime = System.currentTimeMillis();

        List<Long> times = requestTimes.getOrDefault(clientIp,  new ArrayList<>());

        times.removeIf(time -> currentTime - time > TIME_WINDOW);

        if (times.size() >= MAX_REQUESTS) {
            return false;
        }

        times.add( currentTime);
        requestTimes.put(clientIp, times);

        return true;
    }

    public static String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
