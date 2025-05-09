package com.example.sidedemo.User.dto.signUp;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 회원가입 응답 코드 정의
 */
@Getter
public enum SignupResponseCode {
    SIGNUP_SUCCESS      (200,  "회원가입이 정상 처리되었습니다."),
    DUPLICATE_PASSWORD    (4001, "이미 사용 중인 비밀번호입니다."),
    INVALID_VERIFICATION_CODE(4002, "인증 코드가 일치하지 않습니다."),
    EXPIRED_VERIFICATION_CODE(4003, "인증 코드가 만료되었습니다."),
    SERVER_ERROR        (5000, "서버 오류가 발생했습니다.");

    private final int code;
    private final String defaultMessage;

    SignupResponseCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @JsonValue
    public int getCode() {
        return code;
    }
}