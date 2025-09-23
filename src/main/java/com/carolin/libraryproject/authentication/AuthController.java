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

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;
    private final LoginAttemptService loginAttemptService;
    private final JwtRefreshTokenProvider jwtRefreshTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtUtils, TokenBlacklistService tokenBlacklistService, LoginAttemptService loginAttemptService, JwtRefreshTokenProvider jwtRefreshTokenProvider, JwtTokenProvider jwtTokenProvider) {

        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
        this.loginAttemptService = loginAttemptService;
        this.jwtRefreshTokenProvider = jwtRefreshTokenProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }




    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        // Kontroll om användaren finns och om det stämmer med lösenord
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        //  Spara authentication i SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //  Generera access-token
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // Generera refresh-token
        String refreshToken = jwtRefreshTokenProvider.generateRefreshToken(authentication);

        // 5. Hämta användardetaljer
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = jwtTokenProvider.getRoleFromToken(accessToken);

        // 6. Returnera både access och refresh tokens i svaret
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "username", userDetails.getUsername(),
                "role", role
        ));
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(HttpServletRequest request) {

        // Hämtar access-token från Authorization header
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
            tokenBlacklistService.blacklistToken(accessToken); // Gör access-token ogiltig
        }

        // Hämtar refresh-token från en custom header eller request body
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && !refreshToken.isEmpty()) {
            tokenBlacklistService.blacklistToken(refreshToken); // Gör refresh-token ogiltig
        }

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }




    @PostMapping("/refresh")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtRefreshTokenProvider.generateRefreshToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = jwtTokenProvider.getRoleFromToken(accessToken);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "username", userDetails.getUsername(),
                "role", role
        ));
    }




}
