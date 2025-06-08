package com.delipick.user.infrastructure.config;

import com.delipick.user.common.jwt.JwtAuthenticationFilter;
import com.delipick.user.common.jwt.JwtAuthorizationFilter;
import com.delipick.user.common.jwt.JwtUtil;
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

    private final JwtAuthorizationFilter jwtAuthorizationFilter;


    public WebSecurityConfig(JwtUtil jwtUtil, AuthenticationConfiguration authenticationConfiguration, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((authorizationRequests) ->
                        authorizationRequests
                                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
