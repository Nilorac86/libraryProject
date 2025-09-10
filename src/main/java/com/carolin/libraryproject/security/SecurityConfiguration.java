package com.carolin.libraryproject.security;

import com.carolin.libraryproject.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfiguration{


private final AccessDeniedHandler accessDeniedHandler;



    public SecurityConfiguration(AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
        ;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                // .requestMatchers för get kan vara public, post, put och delete behöver inloggning.
                // Sedan ska roller tilldelas och autentisering tilldelas för de olika rollerna.

                .csrf(csrf -> csrf.disable())



                .authorizeHttpRequests(auth -> auth


                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()  // Tillåter alla att skapa konto
                        .requestMatchers(HttpMethod.GET, "/books","/authors/lastname/", "/books/page",
                                "/books/search", "/books/search/author", "books/page", "/authors")
                        .hasAnyRole("USER", "ADMIN")// Användare har bara tillgång till vissa sidor.
                        .requestMatchers(HttpMethod.POST, "/loans").hasAnyRole("USER","ADMIN") // En användare kan skapa ett lån.
                        .requestMatchers(HttpMethod.PUT, "/loans/**").hasAnyRole("ADMIN", "USER") // En användare kan lämna tillbaka sitt lån
                        .requestMatchers(HttpMethod.GET, "/loans").hasRole("ADMIN")
                        .anyRequest().hasRole("ADMIN") // Admin har tillgång till alla sidor
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                )

               .httpBasic(Customizer.withDefaults()) // För att kunna testa i postman

               // .formLogin(Customizer.withDefaults()) // default login
        //.formLogin(form -> form  // Standard inloggning
                        //.loginPage("/login")  // Om man har en frontend
                        // .permitAll())


                .logout(logout -> logout
                        .permitAll())



                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        //.invalidSessionUrl("/login?expired")
                        .sessionFixation(sessionFixation -> sessionFixation.changeSessionId())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                );


        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    }




