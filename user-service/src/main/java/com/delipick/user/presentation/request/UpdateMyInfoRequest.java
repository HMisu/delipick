package com.delipick.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateMyInfoRequest(
        @NotBlank(message = "전화번호는 필수 입력 사항입니다.")
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호는 XXX-XXXX-XXXX와 같은 형식으로 입력해야합니다.")
        String phone,

        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^[가-힣a-zA-Z]{2,20}$",
                message = "이름은 2자 이상 20자 이하의 한글 또는 영문만 입력 가능합니다."
        )
        String name,

        @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$", message = "생일은 yyyy-MM-dd 형식이어야 합니다.")
        String birthdate,

        String address
) {
}
