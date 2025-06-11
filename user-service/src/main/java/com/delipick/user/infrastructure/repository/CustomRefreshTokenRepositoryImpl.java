package com.delipick.user.infrastructure.repository;

import com.delipick.user.domain.model.RefreshToken;
import com.delipick.user.domain.repository.CustomRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomRefreshTokenRepositoryImpl implements CustomRefreshTokenRepository {

    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    @Override
    public void deleteByMemberId(String memberId) {
        List<RefreshToken> tokens = redisRefreshTokenRepository.findAllByMemberId(memberId);
        if (!tokens.isEmpty()) {
            List<String> keysToDelete = tokens.stream()
                    .map(RefreshToken::getRefreshToken)
                    .collect(Collectors.toList());
            redisRefreshTokenRepository.deleteAllById(keysToDelete);
        }
    }
}