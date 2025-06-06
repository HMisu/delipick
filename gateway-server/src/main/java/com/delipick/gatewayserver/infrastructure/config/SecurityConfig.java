package com.delipick.gatewayserver.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        // JWT 토큰 검증 로직 추가 예정
        // 없으면 401 응답 처리 예:
        // return unauthorizedResponse(exchange);

        return chain.filter(exchange);
    }
}