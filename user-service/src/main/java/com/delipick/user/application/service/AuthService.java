package com.delipick.user.application.service;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.UserRepository;
import com.delipick.user.presentation.request.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDto signup(@Valid SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        checkEmailDuplication(request.email());
        checkPhoneDuplication(request.phone());

        User user = User.create(
                request.email(),
                encodedPassword,
                request.phone(),
                request.birthdate(),
                request.address());

        User savedUser = userRepository.save(user);
        return UserDto.of(savedUser);
    }

    private void checkEmailDuplication(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }

    private void checkPhoneDuplication(String phone) {
        if (userRepository.findByPhone(phone).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 전화번호입니다.");

        }
    }
}