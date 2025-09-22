package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// OncePerRequestFilter garanterar att den bara körs engång
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtUtilis;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    public JwtAuthenticationFilter(JwtTokenProvider jwtUtilis, UserDetailsService userDetailsService, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtilis = jwtUtilis;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
    }


    // Filter som kontrollerar om det finns en giltig JWT token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {  // Kontroll av giltig token finns
            String jwt = parseJwt(request);
            if (jwt != null && !tokenBlacklistService.isBlacklisted(jwt) && jwtUtilis.validateToken(jwt)) {
                String username = jwtUtilis.getUsernameFromToken(jwt);
                String role = jwtUtilis.getRoleFromToken(jwt);

                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);

                // Hämtar användarens uppgifter
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);


                // Skapar ett authentication objekt för att veta vem som är inloggad och vilken roll.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken
                        (userDetails, null, List.of(grantedAuthority));

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } // Logg om användaren inte hittas.
        catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        //Skicka vidare till nästa filter eller controller
        chain.doFilter(request, response);
    }


// Hämtar token från headern.
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}
