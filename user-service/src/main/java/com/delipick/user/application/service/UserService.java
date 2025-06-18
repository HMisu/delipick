package com.delipick.user.application.service;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.domain.enums.EmailVerificationPurposeEnum;
import com.delipick.user.domain.enums.UserRoleEnum;
import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.EmailAuthRepository;
import com.delipick.user.domain.repository.UserRepository;
import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import com.delipick.user.presentation.request.ResetPasswordRequest;
import com.delipick.user.presentation.request.UpdateMyInfoRequest;
import com.delipick.user.presentation.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;

    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public void updatedPassword(String userId, String roleStr, UpdatePasswordRequest updatePasswordRequest) {
        validateMemberRole(roleStr);

        if (!updatePasswordRequest.newPassword().equals(updatePasswordRequest.newPasswordConfirm())) {
            throw new CustomException(ErrorCode.INCORRECT_NEW_PASSWORD_CONFIRM);
        }

        User user = findUserById(userId);

        if (!passwordEncoder.matches(updatePasswordRequest.currentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_CURRENT_PASSWORD);
        }

        if (passwordEncoder.matches(updatePasswordRequest.newPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_CURRENT_PASSWORD);
        }

        String encodedPassword = passwordEncoder.encode(updatePasswordRequest.newPassword());
        user.updatePassword(encodedPassword);
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

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }

    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhoneAndIsDeletedFalse(phone);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if (!resetPasswordRequest.newPassword().equals(resetPasswordRequest.newPasswordConfirm())) {
            throw new CustomException(ErrorCode.INCORRECT_NEW_PASSWORD_CONFIRM);
        }

        verifyEmailAuth(resetPasswordRequest.email(), resetPasswordRequest.verifyCode());

        User user = userRepository.findByEmail(resetPasswordRequest.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(resetPasswordRequest.newPassword());
        user.updatePassword(encodedPassword);
    }

    private void verifyEmailAuth(String email, String code) {
        emailAuthRepository.findByEmailAndPurpose(email, EmailVerificationPurposeEnum.RESET_PASSWORD)
                .filter(auth -> auth.getVerifyCode().equals(code))
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE));
    }
}
