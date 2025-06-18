package com.delipick.user.presentation.request;

public record TokenRequestDto(
        String accessToken,
        String refreshToken
) {
}