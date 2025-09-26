package com.carolin.libraryproject.security;

import com.carolin.libraryproject.authentication.JwtAuthenticationEntryPoint;
import com.carolin.libraryproject.authentication.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UserServiceImpl userService;
    private final RateLimitService rateLimitService;



    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthenticationEntryPoint unauthorizedHandler, AccessDeniedHandler accessDeniedHandler, UserServiceImpl userService, RateLimitService rateLimitService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.unauthorizedHandler = unauthorizedHandler;
        this.accessDeniedHandler = accessDeniedHandler;
        this.userService = userService;
        this.rateLimitService = rateLimitService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RateLimitService rateLimitService, RateLimitFilter rateLimitFilter) throws Exception {
        http
                // cors skydd
                .cors(Customizer.withDefaults())
                // csrf disable eftersom appen använder jwt tokens
                .csrf(csrf -> csrf.disable())


                .authorizeHttpRequests(auth -> auth

                        // Öppna endpoints
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()  // Tillåter alla att skapa konto
                        .requestMatchers(HttpMethod.GET, "/books/**","/authors/**").permitAll()

                        // Endpoint som Inloggad användare och admin har tillgång till
                        .requestMatchers(HttpMethod.POST, "/loans").hasAnyRole("USER", "ADMIN") // En användare kan skapa ett lån.
                        .requestMatchers(HttpMethod.PUT, "/loans/**").hasAnyRole("USER", "ADMIN" ) // En användare kan lämna tillbaka sitt lån
                        .requestMatchers(HttpMethod.GET, "/users/loans").hasAnyRole("USER", "ADMIN")

                        // Endpoints som bara admin har tillgång till.
                        .requestMatchers(HttpMethod.POST, "/authors", "/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .anyRequest().hasRole("ADMIN") // Admin har tillgång till alla sidor
                )

                // Specifik exceptionhandling
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(unauthorizedHandler)
                )

                // Tvingar att använda https istället.
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)) // 1år

                        // Hindrar att angripare försöker lura användaren att klicka på något dolt.
                        .frameOptions(frame -> frame.deny())

                        // Bestämmer vilka källor sidan får ladda sina resurser ifrån.
                        // Alla från sin egen domän är det som är inställt.
                        // Skyddar mot xss och andra externa källor
                        //När man har en frontend
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; " +
                                                "script-src 'self'; " +
                                                "style-src 'self'; " +
                                                "img-src 'self';"
                                )))


                // För att kunna testa i postman
               .httpBasic(Customizer.withDefaults())

                    // Sessionmanager används inte och är då inställt på stateless efter som appen använder jwt tokens
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            http        // Lägger till jwt token innan standard inloggningssfilter körs.
                        // För kontroll av giltid token innan varje request.
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            http    // Generellt rate limit filter för alla endpoint.
                    .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


    // Krypterar alla lösenord
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }


    //  Kontrollerar användarnamn och kryprerade lösenord.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    // Skickar login förfrågan till daoAuthenticationProvider. Om det stämmer lyckas authentisieringen.
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




