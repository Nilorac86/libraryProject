package com.carolin.libraryproject.event;

import com.carolin.libraryproject.authentication.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthEventListener.class);
    private final LoginAttemptService loginAttemptService;

    public AuthEventListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }


    @EventListener
    public void handleSuccessfulLogin(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();

        loginAttemptService.loginSuccess(email);

        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "successfulLogin");
        logData.put("username", email);
        logData.put("timestamp", LocalDateTime.now());

        logger.info("User '{}' logged in successfully", logData);
    }



    @EventListener
    public void handleFailedLogin(AbstractAuthenticationFailureEvent event) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String clientIp = "unknown";


        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            if (request != null) {
                clientIp = request.getRemoteAddr();
            }

        }

        String email = event.getAuthentication().getName();

        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "login_failed");
        logData.put("username", event.getAuthentication().getName());
        logData.put("timestamp", LocalDateTime.now().toString());
        logData.put("ipAddress", clientIp);
        logData.put("reason", event.getException().getMessage() );

        logger.warn("Failed login attempt for '{}' ", logData);

        loginAttemptService.loginFaild(email);

        if(loginAttemptService.isBlocked(email)) {
            logger.warn("User '{}' is blocked due to many failed login attempts", email);
        }
    }




    @EventListener
    public void handleLogoutSuccess(LogoutSuccessEvent event) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("event", "logout_success");
        logData.put("username", event.getAuthentication().getName());
        logData.put("timestamp", LocalDateTime.now().toString());

        logger.info("User '{}' logged out successfully", logData);

    }


}
