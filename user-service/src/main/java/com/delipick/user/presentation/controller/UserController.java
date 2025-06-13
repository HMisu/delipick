package com.delipick.user.presentation.controller;

import com.delipick.user.application.service.UserService;
import com.delipick.user.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/withdrawal")
    public ResponseEntity<ApiResponse<String>> deleteUser(@RequestHeader("X-User-Id") String userId,
                                                          @RequestHeader("X-Role") String role) {
        userService.withdrawal(userId, role);
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다."));
    }
}
