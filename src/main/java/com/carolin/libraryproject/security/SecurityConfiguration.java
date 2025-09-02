package com.carolin.libraryproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                // .requestMatchers för get kan vara public, post, put och delete behöver inloggning.
                // Sedan ska roller tilldelas och autentisering tilldelas för de olika rollerna.

                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/books","/authors/lastname/", "/books/page",
                                "/books/search", "/books/search/author", "books/page").hasRole("USER")// Användare har bara tillgång till vissa sidor.
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()  // Tillåter alla att skapa konto
                        .requestMatchers(HttpMethod.POST, "/loans").hasRole("USER") // En användare kan skapa ett lån.
                        .requestMatchers(HttpMethod.PUT, "/loans/").hasRole("USER") // En användare kan lämna tillbaka sitt lån
                        .anyRequest().hasRole("ADMIN") // Admin har tillgång till alla sidor
                )

               // .httpBasic(Customizer.withDefaults()) // För att kunna testa i postman

                .formLogin(Customizer.withDefaults()) // default login
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



//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/public/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/books").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/books").authenticated()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults());

//return http.build();

