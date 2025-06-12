package com.delipick.user.common.jwt;


import com.delipick.user.application.dto.UserDto;
import com.delipick.user.domain.repository.LogoutTokenRepository;
import com.delipick.user.infrastructure.security.UserDetailsServiceImpl;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final LogoutTokenRepository logoutTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.equals("/api/auth/login") || uri.equals("/api/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        if (jwtUtil.isTokenInvalid(token)) {
            chain.doFilter(request, response);
            return;
        }

        if (logoutTokenRepository.existsById(token)) {
            log.info("블랙리스트에 있는 토큰으로 요청이 들어왔습니다.");
            response.setStatus(ErrorCode.LOGOUT_TOKEN_BLACKLISTED.getStatus());
            response.setContentType("application/json;charset=UTF-8");
            String body = String.format("{\"code\":\"%s\", \"message\":\"%s\"}",
                    ErrorCode.LOGOUT_TOKEN_BLACKLISTED.getCode(),
                    ErrorCode.LOGOUT_TOKEN_BLACKLISTED.getMessage());
            response.getWriter().write(body);
            response.getWriter().flush();
            return;
        }

        try {
            UserDto userDto = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(userDto.getEmail());
        } catch (Exception e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
            // 인증 실패해도 계속 진행
        }

        chain.doFilter(request, response);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email, null);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public Authentication createAuthentication(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}