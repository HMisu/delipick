package com.delipick.user.common.jwt;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.domain.model.RefreshToken;
import com.delipick.user.domain.repository.CustomRefreshTokenRepository;
import com.delipick.user.domain.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomRefreshTokenRepository customRefreshTokenRepository;

    @Value(("${jwt.access-expiration}"))
    private Long accessExpiration;
    @Value(("${jwt.refresh-expiration}"))
    private Long refreshExpiration;

    public static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey, RefreshTokenRepository refreshTokenRepository, CustomRefreshTokenRepository customRefreshTokenRepository) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.refreshTokenRepository = refreshTokenRepository;
        this.customRefreshTokenRepository = customRefreshTokenRepository;
    }

    public String createAccessToken(Long id, String email, String username, UserRoleEnum role) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("role", role.toString())
                .claim("name", username)
                .issuer("delipick")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long id) {
        customRefreshTokenRepository.deleteByMemberId(id.toString());

        String refreshToken = Jwts.builder()
                .subject(id.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();

        RefreshToken tokenEntity = new RefreshToken(refreshToken, id.toString());
        RefreshToken savedToken = refreshTokenRepository.save(tokenEntity);

        return savedToken.getRefreshToken();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("JWT expired: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    public UserDto getUserInfoFromToken(String token) {
        Claims claims = parseClaims(token);

        Long id = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        return UserDto.builder()
                .id(id)
                .email(email)
                .name(name)
                .role(UserRoleEnum.valueOf(role))
                .build();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
