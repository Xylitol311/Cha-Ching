package com.example.chaching.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
