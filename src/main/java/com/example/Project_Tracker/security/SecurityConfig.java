package com.example.Project_Tracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public routes
                        // .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(SecurityConstants.PUBLIC_ENDPOINTS).permitAll()

                        // Admin routes
                        .requestMatchers("/api/auth/register").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/ibus/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/ibus/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ADMIN")

                        // Manager routes
                        .requestMatchers("/api/projects/**").hasAnyAuthority("MANAGER", "ADMIN")
                        .requestMatchers("/api/milestones/**").hasAnyAuthority("MANAGER", "PROJECT_MANAGER")
                        .requestMatchers("/api/members/**").hasAnyAuthority("MANAGER", "PROJECT_MANAGER")

                        // Employee routes
                        .requestMatchers("/api/tasks/**").hasAnyAuthority("EMPLOYEE", "PROJECT_MANAGER", "MANAGER")

                        // All others
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint) // handles 401
                        .accessDeniedHandler(accessDeniedHandler) // handles 403
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
