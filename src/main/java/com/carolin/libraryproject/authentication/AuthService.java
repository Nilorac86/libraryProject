package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.exceptionHandler.TooManyRequestsException;
import com.carolin.libraryproject.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {


        private final LoginAttemptService loginAttemptService;
        private final RateLimitService rateLimitService;
        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;
        private final JwtRefreshTokenProvider jwtRefreshTokenProvider;
        private final TokenBlacklistService tokenBlacklistService;


    public AuthService(LoginAttemptService loginAttemptService, RateLimitService rateLimitService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, JwtRefreshTokenProvider jwtRefreshTokenProvider, TokenBlacklistService tokenBlacklistService) {
        this.loginAttemptService = loginAttemptService;
        this.rateLimitService = rateLimitService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public Map<String, String> login(String email, String password, String clientIP) {
            //  Rate limit per IP
            if (!rateLimitService.isAllowed(clientIP)) {
                throw new TooManyRequestsException("Too many requests. Try again later.");
            }

            // 2. Temporär blockering per användare
            if (loginAttemptService.isTemporarilyBlocked(email)) {
                throw new TooManyRequestsException("The account is temporarily blocked. Try again later.");
            }

        // 3. Autentisera användare
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Om login lyckas → nollställ misslyckade försök
        loginAttemptService.loginSuccess(email);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtRefreshTokenProvider.generateRefreshToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = jwtTokenProvider.getRoleFromToken(accessToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "username", userDetails.getUsername(),
                "role", role
        );

    }


    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
            tokenBlacklistService.blacklistToken(accessToken);
        }
        if (refreshToken != null && !refreshToken.isEmpty()) {
            tokenBlacklistService.blacklistToken(refreshToken);
        }
    }


    public Map<String, String> refresh(String refreshToken) {
        if (!jwtRefreshTokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String username = jwtRefreshTokenProvider.getUsernameFromRefreshToken(refreshToken);
        String role = jwtRefreshTokenProvider.getRoleFromRefreshToken(refreshToken);

        // Skapa ny access-token
        String newAccessToken = jwtTokenProvider.generateTokenFromUsernameAndRole(username, role);

        return Map.of(
                "accessToken", newAccessToken,
                "username", username,
                "role", role
        );
    }



//
//    public Map<String, String> refresh(String email, String password) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(email, password)
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//
//        String accessToken = jwtTokenProvider.generateToken(authentication);
//        String refreshToken = jwtRefreshTokenProvider.generateRefreshToken(authentication);
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        String role = jwtTokenProvider.getRoleFromToken(accessToken);
//
//        return Map.of(
//                "accessToken", accessToken,
//                "refreshToken", refreshToken,
//                "username", userDetails.getUsername(),
//                "role", role
//        );
//    }
}


