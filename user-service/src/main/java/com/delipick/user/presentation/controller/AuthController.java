package com.delipick.user.presentation.controller;

import com.delipick.user.application.dto.TokenResponseDto;
import com.delipick.user.application.dto.UserDto;
import com.delipick.user.application.service.AuthService;
import com.delipick.user.common.dto.ApiResponse;
import com.delipick.user.presentation.request.ReactivateRequest;
import com.delipick.user.presentation.request.SignupRequest;
import com.delipick.user.presentation.request.TokenRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody SignupRequest signupRequest) {
        UserDto createdUser = authService.register(signupRequest);

        return ResponseEntity.ok(ApiResponse.success(createdUser));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequestDto requestDto) {
        TokenResponseDto responseDto = authService.refreshToken(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다."));
    }

    @PatchMapping("/reactivate")
    public ResponseEntity<ApiResponse<String>> reactivateAccount(@RequestBody ReactivateRequest request) {
        authService.reactivateUser(request.email());
        return ResponseEntity.ok(ApiResponse.success("계정이 성공적으로 복구되었습니다."));
    }
}
