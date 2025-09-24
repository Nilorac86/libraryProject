package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.authentication.authDto.JwtResponseDto;
import com.carolin.libraryproject.authentication.authDto.LoginRequestDto;
import com.carolin.libraryproject.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.carolin.libraryproject.authentication.RateLimitService.getClientIP;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto,
                                   HttpServletRequest request) {

        String clientIP = RateLimitService.getClientIP(request);

        Map<String, String> tokens = authService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword(),
                clientIP
        );
        return ResponseEntity.ok(tokens);
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("Refresh-Token");
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody LoginRequestDto loginRequestDto) {
        Map<String, String> tokens = authService.refresh(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );
        return ResponseEntity.ok(tokens);
    }
}