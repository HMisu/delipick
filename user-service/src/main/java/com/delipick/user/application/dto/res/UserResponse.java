package com.delipick.user.application.dto.res;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.domain.enums.UserRoleEnum;

public record UserResponse(
        Long id,
        String email,
        String phone,
        String birthdate,
        String address,
        UserRoleEnum role
) {
    public static UserResponse from(UserDto dto) {
        return new UserResponse(
                dto.id(),
                dto.email(),
                dto.phone(),
                dto.birthdate(),
                dto.address(),
                dto.role()
        );
    }
}