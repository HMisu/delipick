package com.delipick.user.infrastructure.repository;

import com.delipick.user.domain.model.RefreshToken;
import com.delipick.user.domain.repository.RefreshTokenRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedisRefreshTokenRepository extends CrudRepository<RefreshToken, String>, RefreshTokenRepository {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    List<RefreshToken> findAllByMemberId(String memberId);
}