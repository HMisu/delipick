package com.delipick.user.presentation.exception.handler;

import com.delipick.user.common.dto.ApiResponse;
import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<String>> buildErrorResponse(ErrorCode errorCode) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(errorCode.getStatus())
                .statusMessage(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException ex) {
        log.error("[CustomException] {}", ex.getMessage());
        return buildErrorResponse(ex.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        log.error("[Exception] {}", ex.getMessage(), ex);
        return buildErrorResponse(ErrorCode.INTERNAL_ERROR);
    }
}