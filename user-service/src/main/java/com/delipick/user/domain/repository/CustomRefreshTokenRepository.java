package com.delipick.user.domain.repository;

public interface CustomRefreshTokenRepository {
    void deleteByMemberId(String memberId);
}