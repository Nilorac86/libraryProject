package com.carolin.libraryproject.security;

import com.carolin.libraryproject.authentication.AuthEntryPointJwt;
import com.carolin.libraryproject.authentication.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfiguration{

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UserServiceImpl userService;



    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthEntryPointJwt unauthorizedHandler, AccessDeniedHandler accessDeniedHandler, UserServiceImpl userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userService = userService;
        ;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                // .requestMatchers för get kan vara public, post, put och delete behöver inloggning.
                // Sedan ska roller tilldelas och autentisering tilldelas för de olika rollerna.

                .csrf(csrf -> csrf.disable())



                .authorizeHttpRequests(auth -> auth


                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()  // Tillåter alla att skapa konto
                        .requestMatchers(HttpMethod.GET, "/books","/authors/lastname/", "/books/page",
                                "/books/search", "/books/search/author", "/books/page", "/authors")
                        .hasAnyRole("USER", "ADMIN")// Användare har bara tillgång till vissa sidor.
                        .requestMatchers(HttpMethod.POST, "/authors", "/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/loans").hasRole("USER") // En användare kan skapa ett lån.
                        .requestMatchers(HttpMethod.PUT, "/loans/**").hasRole("USER") // En användare kan lämna tillbaka sitt lån
                        .requestMatchers(HttpMethod.GET, "/loans").hasRole("USER")
                        .anyRequest().hasRole("ADMIN") // Admin har tillgång till alla sidor
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(unauthorizedHandler)
                )

               .httpBasic(Customizer.withDefaults()) // För att kunna testa i postman

               //.formLogin(Customizer.withDefaults()) // default login
        //.formLogin(form -> form  // Standard inloggning
                        //.loginPage("/login")  // Om man har en frontend



                .logout(logout -> logout
                        .permitAll())



                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                        //.invalidSessionUrl("/login?expired")
//
//                        .maximumSessions(1).sessionFixation(sessionFixation -> sessionFixation.changeSessionId())
//                        .maxSessionsPreventsLogin(false)


            http
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }



    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider)
                .build();

    }

    }


//    {
//            "email": "anna.andersson@email.com",
//            "password": "password123"
//            }



