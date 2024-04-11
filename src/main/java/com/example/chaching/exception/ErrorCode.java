package com.example.chaching.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),

    // 회원가입 에러
    USERID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),

    // JWT 로그인 에러
    INVALID_JWT_SIGNATURE(HttpStatus.FORBIDDEN, "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.FORBIDDEN, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.FORBIDDEN, "지원되지 않는 JWT 토큰입니다."),
    JWT_CLAIMS_IS_EMPTY(HttpStatus.FORBIDDEN, "잘못된 JWT 토큰입니다.");


    private final HttpStatus status;
    private final String message;
}
