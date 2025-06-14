package com.delipick.user.infrastructure.config;

import com.delipick.user.common.jwt.JwtAuthenticationFilter;
import com.delipick.user.common.jwt.JwtAuthorizationFilter;
import com.delipick.user.common.jwt.JwtUtil;
import com.delipick.user.infrastructure.security.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public WebSecurityConfig(JwtUtil jwtUtil,
                             AuthenticationConfiguration authenticationConfiguration,
                             CustomAuthenticationEntryPoint authenticationEntryPoint,
                             JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/reactivate").permitAll()
                        .requestMatchers("/api/users/email-exists", "/api/users/phone-exists").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint));

        http.addFilterAt(
                new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, authenticationEntryPoint),
                UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
