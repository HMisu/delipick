package com.delipick.user.application.dto;

import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.domain.model.User;

public record UserDto(
        Long id,
        String email,
        String phone,
        String birthdate,
        String address,
        UserRoleEnum role
) {

    public static UserDto of(final User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthdate(),
                user.getAddress(),
                user.getRole()
        );
    }
}
