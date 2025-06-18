package com.delipick.user.presentation.request;

import com.delipick.user.domain.enums.EmailVerificationPurposeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailVerificationRequest(
        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotNull(message = "인증 목적은 필수입니다.")
        EmailVerificationPurposeEnum purpose
) {
}