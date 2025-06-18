package com.delipick.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "blackList", timeToLive = 3600)
public class LogoutToken {
    @Id
    private String accessToken;
}