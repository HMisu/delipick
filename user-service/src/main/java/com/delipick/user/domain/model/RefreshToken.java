package com.delipick.user.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken {
    @Id
    private String refreshToken;

    @Indexed
    private String memberId;

    @JsonCreator
    public RefreshToken(@JsonProperty("refreshToken") String refreshToken, @JsonProperty("memberId") String memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}