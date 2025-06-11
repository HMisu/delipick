package com.delipick.user.infrastructure.repository;

import com.delipick.user.domain.model.RefreshToken;
import com.delipick.user.domain.repository.RefreshTokenRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisRefreshTokenRepository extends CrudRepository<RefreshToken, String>, RefreshTokenRepository {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByMemberId(String memberId);
}