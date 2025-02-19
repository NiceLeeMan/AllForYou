package com.example.sidedemo.store;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationCodeStore {

    // 이메일(or 유저ID)를 key로, 인증코드를 value로
    private final Map<String, String> codeMap = new ConcurrentHashMap<>();

    public void saveCode(String key, String code) {
        codeMap.put(key, code);
    }

    public String getCode(String key) {
        return codeMap.get(key);
    }

    public void removeCode(String key) {
        codeMap.remove(key);
    }
}