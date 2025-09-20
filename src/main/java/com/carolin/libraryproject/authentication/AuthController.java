package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.authentication.authDto.JwtResponseDto;
import com.carolin.libraryproject.authentication.authDto.LoginRequestDto;
import com.carolin.libraryproject.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController { // Lägga till logout


    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtUtils;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtUtils, TokenBlacklistService tokenBlacklistService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // Loggar in och genererar ett JWT token om användaren är autentiserad
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser (@RequestBody LoginRequestDto loginRequestDto) {

        // Kontroll om användaren finns och om det stämmer med lösenord
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        // Sparar i spring security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Skapar en JWT token
        String token = jwtUtils.generateToken(authentication);

        // Hämtar användaren och dess roll.
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = userDetails.getUser().getRole();


        // Returnerar en ny JWT response med token för andra api anrop, användare och dess roll
        return ResponseEntity.ok(new JwtResponseDto(token,
                userDetails.getUsername(), role));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser (HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        tokenBlacklistService.blacklistToken(token);

        return ResponseEntity.ok( Map.of("message", "Logged out successefully"));
    }

}
