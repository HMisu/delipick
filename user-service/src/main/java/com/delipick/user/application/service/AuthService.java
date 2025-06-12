package com.delipick.user.application.service;

import com.delipick.user.application.dto.TokenResponseDto;
import com.delipick.user.application.dto.UserDto;
import com.delipick.user.common.jwt.JwtUtil;
import com.delipick.user.domain.model.LogoutToken;
import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.LogoutTokenRepository;
import com.delipick.user.domain.repository.RefreshTokenRepository;
import com.delipick.user.domain.repository.UserRepository;
import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import com.delipick.user.presentation.request.SignupRequest;
import com.delipick.user.presentation.request.TokenRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutTokenRepository logoutTokenRepository;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserDto register(@Valid SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        checkEmailDuplication(request.email());
        checkPhoneDuplication(request.phone());

        User user = User.create(
                request.email(),
                encodedPassword,
                request.phone(),
                request.name(),
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

    public TokenResponseDto refreshToken(TokenRequestDto requestDto) {
        String accessToken = requestDto.accessToken();
        String refreshToken = requestDto.refreshToken();

        if (jwtUtil.isTokenInvalid(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (refreshTokenRepository.findByRefreshToken(refreshToken).isEmpty()) {
            log.warn("Invalid refresh token used: {}", maskToken(refreshToken));
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        UserDto userInfo = jwtUtil.getUserInfoFromToken(accessToken);
        Long userId = userInfo.getId();

        String newAccessToken = jwtUtil.createAccessToken(userId, userInfo.getEmail(), userInfo.getName(), userInfo.getRole());
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    private String maskToken(String token) {
        if (token.length() <= 10) return token;
        return token.substring(0, 5) + "****" + token.substring(token.length() - 5);
    }

    public void logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return;
        }
        String accessToken = authorizationHeader.substring(7);
        log.info("accessToken: {}", maskToken(accessToken));
        try {
            logoutTokenRepository.save(new LogoutToken(accessToken));
        } catch (Exception e) {
            throw new CustomException(ErrorCode.LOGOUT_TOKEN_SAVE_FAILURE);
        }
    }

}