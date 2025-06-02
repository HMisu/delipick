package com.delipick.user.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    ROLE_USER,
    ROLE_SELLER,
    ROLE_ADMIN,
    ROLE_MASTER;
}
