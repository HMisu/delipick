package com.delipick.user.presentation.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT(400, "E4001", "잘못된 요청입니다."),
    INVALID_REFRESH_TOKEN(401, "E4011", "리프레시 토큰이 유효하지 않습니다."),
    UNAUTHORIZED(401, "E4012", "인증이 필요합니다."),
    LOGOUT_TOKEN_BLACKLISTED(401, "E4013", "로그아웃된 토큰으로 접근할 수 없습니다."),
    FORBIDDEN_NOT_SELF(403, "E4031", "본인의 정보에만 접근할 수 있습니다."),
    FORBIDDEN_ADMIN_ONLY(403, "E4032", "관리자만 접근할 수 있는 기능입니다."),
    USER_NOT_FOUND(404, "E4041", "해당 사용자를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "E4042", "저장된 리프레시 토큰이 없거나 로그아웃된 상태입니다."),
    EMAIL_ALREADY_EXISTS(409, "E4091", "이미 가입된 이메일입니다."),
    PHONE_ALREADY_EXISTS(409, "E4092", "이미 가입된 전화번호입니다."),
    INCORRECT_CURRENT_PASSWORD(400, "E4002", "현재 비밀번호가 일치하지 않습니다."),
    LOGOUT_TOKEN_SAVE_FAILURE(500, "E5002", "로그아웃 토큰 저장에 실패했습니다."),
    USER_DELETION_FAILED(500, "E5003", "회원 탈퇴에 실패했습니다."),
    INTERNAL_ERROR(500, "E5001", "서버 내부 오류입니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(this.status);
    }
}