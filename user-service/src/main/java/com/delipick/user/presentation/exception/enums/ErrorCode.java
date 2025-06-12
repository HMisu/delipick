package com.delipick.user.presentation.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_INPUT(400, "E4001", "잘못된 요청입니다."),
    INVALID_REFRESH_TOKEN(401, "E4011", "리프레시 토큰이 유효하지 않습니다."),
    UNAUTHORIZED(401, "E4012", "인증이 필요합니다."),
    USER_NOT_FOUND(404, "E4041", "해당 사용자를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "E4042", "저장된 리프레시 토큰이 없거나 로그아웃된 상태입니다."),
    LOGOUT_TOKEN_SAVE_FAILURE(500, "E5002", "로그아웃 토큰 저장에 실패했습니다."),
    LOGOUT_TOKEN_SAVED(200, "S2001", "로그아웃 토큰이 블랙리스트에 저장되었습니다."),
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