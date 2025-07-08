package com.example.sidedemo.User.common.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class VerificationCodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public String generate6DigitCode() {
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }
}
