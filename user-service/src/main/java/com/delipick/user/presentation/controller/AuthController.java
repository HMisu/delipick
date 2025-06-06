package com.delipick.user.presentation.controller;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.application.service.AuthService;
import com.delipick.user.common.dto.ApiResponse;
import com.delipick.user.presentation.request.SignupRequest;
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

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        UserDto createdUser = authService.signup(signupRequest);

        return ResponseEntity.ok(ApiResponse.success(createdUser));
    }
}
