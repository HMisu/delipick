package com.delipick.user.common.jwt;


import com.delipick.user.infrastructure.security.UserDetailsServiceImpl;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        log.info("request : {}", request.getHeader("X-User-Id"));
        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");

        if (userId != null && userRole != null && email != null) {
            try {
                setAuthentication(email);
            } catch (Exception e) {
                log.error("JWT 인증 실패: {}", e.getMessage());
                // 인증 실패해도 계속 진행
            }
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