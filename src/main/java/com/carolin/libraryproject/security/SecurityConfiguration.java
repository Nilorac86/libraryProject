package com.carolin.libraryproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
                        .requestMatchers(HttpMethod.GET, "/books").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )

                //.httpBasic(Customizer.withDefaults());
                .formLogin(form -> form.permitAll())

                .logout(logout -> logout.permitAll());



        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();

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

