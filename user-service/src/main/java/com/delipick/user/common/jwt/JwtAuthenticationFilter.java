package com.delipick.user.common.jwt;

import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.infrastructure.security.UserDetailsImpl;
import com.delipick.user.presentation.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

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
        log.info("로그인 성공 및 JWT 생성");
        Long id = ((UserDetailsImpl) auth.getPrincipal()).getUser().getId();
        String email = ((UserDetailsImpl) auth.getPrincipal()).getUser().getEmail();
        UserRoleEnum role = ((UserDetailsImpl) auth.getPrincipal()).getUser().getRole();
        String name = ((UserDetailsImpl) auth.getPrincipal()).getUser().getName();

        String token = jwtUtil.createToken(id, email, role, name);

        Map<String, String> tokenMap = Map.of("accessToken", token);
        String jsonResponse = new ObjectMapper().writeValueAsString(tokenMap);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}
