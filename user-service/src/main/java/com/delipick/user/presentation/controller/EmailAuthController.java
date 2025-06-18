package com.delipick.user.presentation.controller;

import com.delipick.user.application.service.EmailAuthService;
import com.delipick.user.common.dto.ApiResponse;
import com.delipick.user.presentation.request.EmailVerificationCheckRequest;
import com.delipick.user.presentation.request.EmailVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-auth")
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailAuthService emailAuthService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> sendCode(@RequestBody EmailVerificationRequest request) {
        emailAuthService.sendCode(request.email(), request.purpose());
        return ResponseEntity.ok(ApiResponse.success("인증 메일이 전송되었습니다."));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(@RequestBody EmailVerificationCheckRequest request) {
        boolean result = emailAuthService.verifyCode(request.email(), request.code(), request.purpose());
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
