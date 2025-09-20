package com.carolin.libraryproject.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;


// Klass som används när autentisering krävs men det inte finns någon token eller om token är ogiltig
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    // Skickar respons om användaren inte är auktoriserad.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized: " + authException.getMessage() + "\"}");
    }
}
