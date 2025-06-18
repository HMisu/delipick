package com.delipick.user.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @NotBlank
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        String verifyCode,

        @NotBlank(message = "새 비밀번호는 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
                message = "비밀번호는 반드시 하나 이상의 대문자와 하나 이상의 소문자, 하나 이상의 숫자, 하나 이상의 특수문자가 필요합니다."
        )
        String newPassword,

        @NotBlank(message = "새 비밀번호 확인은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
                message = "비밀번호는 반드시 하나 이상의 대문자와 하나 이상의 소문자, 하나 이상의 숫자, 하나 이상의 특수문자가 필요합니다."
        )
        String newPasswordConfirm
) {
}