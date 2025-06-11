package com.delipick.user.domain.repository;

import com.delipick.user.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByMemberId(String memberId);

    void save(RefreshToken refreshToken);
}