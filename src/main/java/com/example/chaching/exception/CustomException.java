package com.example.chaching.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;

    @Override
    public String toString() {
        return errorCode + " : " + errorCode.getMessage();
    }
}
