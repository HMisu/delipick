package com.delipick.user.application.service;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.UserRepository;
import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import com.delipick.user.presentation.request.UpdateMyInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void withdrawal(String userId, String roleStr) {
        validateMemberRole(roleStr);
        User user = findUserById(userId);
        user.markAsDeleted(user.getEmail());
    }

    public UserDto getMyInfo(String userId, String roleStr) {
        validateMemberRole(roleStr);
        User user = findUserById(userId);
        return UserDto.of(user);
    }

    @Transactional
    public void updatedMyInfo(String userId, String roleStr, UpdateMyInfoRequest updateMyInfoRequest) {
        validateMemberRole(roleStr);
        User user = findUserById(userId);
        user.update(
                updateMyInfoRequest.phone(),
                updateMyInfoRequest.name(),
                updateMyInfoRequest.birthdate(),
                updateMyInfoRequest.address()
        );
    }

    private void validateMemberRole(String roleStr) {
        UserRoleEnum role = UserRoleEnum.fromString(roleStr);
        validateRoles(role, UserRoleEnum.ROLE_USER, UserRoleEnum.ROLE_SELLER);
    }

    private void validateRoles(UserRoleEnum role, UserRoleEnum... allowedRoles) {
        for (UserRoleEnum allowed : allowedRoles) {
            if (allowed == role) {
                return;
            }
        }
        throw new CustomException(ErrorCode.UNAUTHORIZED);
    }

    private User findUserById(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
