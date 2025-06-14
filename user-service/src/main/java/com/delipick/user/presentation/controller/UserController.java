package com.delipick.user.presentation.controller;

import com.delipick.user.application.dto.UserDto;
import com.delipick.user.application.service.UserService;
import com.delipick.user.common.dto.ApiResponse;
import com.delipick.user.presentation.request.UpdateMyInfoRequest;
import com.delipick.user.presentation.request.UpdatePasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getMyInfo(@RequestHeader("X-User-Id") String userId,
                                                          @RequestHeader("X-Role") String role) {
        UserDto userDto = userService.getMyInfo(userId, role);
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<String>> updatedMyInfo(@RequestHeader("X-User-Id") String userId,
                                                             @RequestHeader("X-Role") String role,
                                                             @Valid @RequestBody UpdateMyInfoRequest updateMyInfoRequest) {
        userService.updatedMyInfo(userId, role, updateMyInfoRequest);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정이 완료되었습니다."));
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<String>> updatedPassword(@RequestHeader("X-User-Id") String userId,
                                                               @RequestHeader("X-Role") String role,
                                                               @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatedPassword(userId, role, updatePasswordRequest);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정이 완료되었습니다."));
    }

    @GetMapping("/email-exists")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = userService.isEmailExists(email);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    @GetMapping("/phone-exists")
    public ResponseEntity<ApiResponse<Boolean>> checkPhoneExists(@RequestParam String phone) {
        boolean exists = userService.isPhoneExists(phone);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
