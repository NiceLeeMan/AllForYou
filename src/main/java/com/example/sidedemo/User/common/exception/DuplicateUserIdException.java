package com.example.sidedemo.User.common.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 이미 사용 중인 아이디일 때 던질 예외
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(String message) {
        super(message);
    }
}
