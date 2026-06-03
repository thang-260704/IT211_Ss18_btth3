package com.example.it211_ss18_btth3.config;

import com.example.it211_ss18_btth3.jwt.JwtAuthenticationFilter;
import com.example.it211_ss18_btth3.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(

                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/products")
                        .authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/products")
                        .hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(HttpMethod.PUT, "/api/products/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(HttpMethod.DELETE, "/api/products/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(HttpMethod.POST, "/api/orders")
                        .hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/orders/my")
                        .hasRole("CUSTOMER")

                        .requestMatchers(HttpMethod.GET, "/api/orders")
                        .hasAnyRole("STAFF", "ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/status")
                        .hasRole("STAFF")

                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}