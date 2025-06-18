package com.delipick.user.common.jwt;

import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.infrastructure.security.UserDetailsImpl;
import com.delipick.user.presentation.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtUtil jwtUtil,
                                   AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.authenticationEntryPoint = authenticationEntryPoint;
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password(),
                            null
                    ));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException {
        Long id = ((UserDetailsImpl) auth.getPrincipal()).getUser().getId();
        String email = ((UserDetailsImpl) auth.getPrincipal()).getUser().getEmail();
        String name = ((UserDetailsImpl) auth.getPrincipal()).getUser().getName();
        UserRoleEnum role = ((UserDetailsImpl) auth.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(id, email, name, role);
        String refreshToken = jwtUtil.createRefreshToken(id);

        Map<String, String> tokenMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
        String jsonResponse = new ObjectMapper().writeValueAsString(tokenMap);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        authenticationEntryPoint.commence(request, response, failed);
    }
}
