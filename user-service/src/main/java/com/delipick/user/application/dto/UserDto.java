package com.delipick.user.application.dto;

import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String birthdate;
    private String address;
    private UserRoleEnum role;

    public static UserDto of(final User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .birthdate(user.getBirthdate())
                .address(user.getAddress())
                .role(user.getRole())
                .build();
    }
}
