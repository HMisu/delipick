package com.delipick.user.application.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
