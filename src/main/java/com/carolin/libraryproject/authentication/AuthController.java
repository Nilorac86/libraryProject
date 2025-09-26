package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.authentication.authDto.LoginRequestDto;
import com.carolin.libraryproject.security.RateLimitService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // Inloggning som genererar en access token och en refresh token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto,
                                   HttpServletRequest request) {

        // Hämtar användarens ipAdress
        String clientIP = RateLimitService.getClientIP(request);


        // anropar service med användarens input
        Map<String, String> tokens = authService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword(),
                clientIP
        );
        return ResponseEntity.ok(tokens);
    }


    // Loggar ut användaren
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {

        // Skickar tokens och användarens uppgifter till service
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("Refresh-Token");
        authService.logout(accessToken, refreshToken);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

// Refreshar token genom att använda den i header.
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        // Läser från header
        String refreshToken = request.getHeader("Refresh-Token");

        // Om det inte finns hen header kontrollerar den cookies
        if (refreshToken == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // Om ingen refresh token finns returneras ett felmeddelande.
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
        }

        // Anropar servicelagret.
        Map<String, String> tokens = authService.refresh(refreshToken);

        // Returnerar ny access token
        return ResponseEntity.ok(Map.of(
                "accessToken", tokens.get("accessToken"),
                "username", tokens.get("username"),
                "role", tokens.get("role")
        ));
    }

}