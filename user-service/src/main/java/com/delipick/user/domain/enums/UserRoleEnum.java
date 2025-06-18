package com.delipick.user.domain.enums;

import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    ROLE_USER,
    ROLE_SELLER,
    ROLE_ADMIN,
    ROLE_MASTER;

    public static UserRoleEnum fromString(String roleStr) {
        try {
            return UserRoleEnum.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }
    }
}