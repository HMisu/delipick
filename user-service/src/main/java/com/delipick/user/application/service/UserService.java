package com.delipick.user.application.service;

import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.UserRepository;
import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void withdrawal(String userId, String roleStr) {
        UserRoleEnum role = UserRoleEnum.fromString(roleStr);
        validateRoles(role, UserRoleEnum.ROLE_USER, UserRoleEnum.ROLE_SELLER);

        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.markAsDeleted(user.getEmail());
    }

    private void validateRoles(UserRoleEnum role, UserRoleEnum... allowedRoles) {
        if (Arrays.stream(allowedRoles).noneMatch(role::equals)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

}
