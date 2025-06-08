package com.delipick.user.common.jwt;

import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.infrastructure.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    @Value(("${jwt.access-expiration}"))
    private Long accessExpiration;

    public static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey, UserDetailsServiceImpl userDetailsService) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createToken(Long id, String email, UserRoleEnum role, String username) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role)
                .claim("name", username)
                .issuer("delipick")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

}
