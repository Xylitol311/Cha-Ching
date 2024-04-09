package com.example.chaching.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),
    USERID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "UserID already exists."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Email already exists."),
    PHONE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Phone Number already exists."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found.");


    private final HttpStatus status;
    private final String message;
}
