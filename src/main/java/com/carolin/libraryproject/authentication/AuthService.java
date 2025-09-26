package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.exceptionHandler.TooManyRequestsException;
import com.carolin.libraryproject.security.CustomUserDetails;
import com.carolin.libraryproject.security.RateLimitService;
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

    // login
    public Map<String, String> login(String email, String password, String clientIP) {
            //  Rate limit per IP
            if (!rateLimitService.isAllowed(clientIP)) {
                throw new TooManyRequestsException("Too many requests. Try again later.");
            }

            // Temporär blockering av användare
            if (loginAttemptService.isTemporarilyBlocked(email)) {
                throw new TooManyRequestsException("The account is temporarily blocked. Try again later.");
            }

        // Autentisera användare
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Om login lyckas nollställs lista med misslyckade logginförsök
        loginAttemptService.loginSuccess(email);

        // Sparar användare i spring security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Skapar JWT både access, refresh, hämtar användares detaljer och roll.
        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtRefreshTokenProvider.generateRefreshToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = jwtTokenProvider.getRoleFromToken(accessToken);


        // Returnerar tokens, användarenamn och roll
        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "username", userDetails.getUsername(),
                "role", role
        );

    }


    // loggar ut användaren samt svartlistar tokens som använts.
    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
            tokenBlacklistService.blacklistToken(accessToken);
        }
        if (refreshToken != null && !refreshToken.isEmpty()) {
            tokenBlacklistService.blacklistToken(refreshToken);
        }
    }


    // Refreshar token
    public Map<String, String> refresh(String refreshToken) {
        // Kontroll om token är ogiltig eller inte finns
        if (!jwtRefreshTokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        // Hämtar användarnamn och roll från tidigare token
        String username = jwtRefreshTokenProvider.getUsernameFromRefreshToken(refreshToken);
        String role = jwtRefreshTokenProvider.getRoleFromRefreshToken(refreshToken);

        // Skapa ny access-token baserat på användarnamn och roll
        String newAccessToken = jwtTokenProvider.generateTokenFromUsernameAndRole(username, role);

        return Map.of(
                "accessToken", newAccessToken,
                "username", username,
                "role", role
        );
    }
}


