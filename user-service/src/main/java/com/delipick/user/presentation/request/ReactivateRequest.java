package com.delipick.user.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ReactivateRequest(
        @NotBlank
        @Email
        String email
) {
}
