package com.carolin.libraryproject.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins( "http://localhost:3000") // Bara på localhost
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Endast tillåtna crud metoder
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight för 1 timme
    }

}



