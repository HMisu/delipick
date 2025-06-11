package com.delipick.user.presentation.controller;

import com.delipick.user.application.dto.TokenResponseDto;
import com.delipick.user.application.dto.UserDto;
import com.delipick.user.application.service.AuthService;
import com.delipick.user.common.dto.ApiResponse;
import com.delipick.user.presentation.request.SignupRequest;
import com.delipick.user.presentation.request.TokenRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
