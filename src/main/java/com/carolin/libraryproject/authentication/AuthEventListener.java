package com.carolin.libraryproject.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthEventListener.class);

    @EventListener
    public void handleSuccessfulLogin(AuthenticationSuccessEvent event) {

        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "successfulLogin");
        logData.put("username", event.getAuthentication().getName());
        logData.put("timestamp", LocalDateTime.now());

        logger.info("User '{}' logged in successfully", logData);
    }




    @EventListener
    public void handleFailedLogin(AbstractAuthenticationFailureEvent event) {

        Map<String, Object> logData = new HashMap<>();

        logData.put("event", "login_failed");
        logData.put("username", event.getAuthentication().getName());
        logData.put("timestamp", LocalDateTime.now().toString());
        logData.put("reason", event.getException().getMessage() );

        logger.warn("Failed login attempt for '{}' ", logData);
    }
}
