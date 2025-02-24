package com.example.sidedemo.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {


    // 실제 서비스에서는 이 키를 안전하게 보관해야 합니다. (예: 환경 변수 또는 Vault 사용)
    @Value("${jwt.secret}")
    private String secretKey;


    @Value("${jwt.expiration}")
    private long jwtExpirationInMillis;

    /**
     * 주어진 subject(예: userId)를 기반으로 JWT 토큰을 생성한다.
     * 토큰 유효기간은 10시간으로 설정.
     */
    public String generateToken(String subject) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 1000 * 60 * 60; // 10 hours

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // 필요에 따라 토큰 검증 메서드 등을 추가할 수 있습니다.
}
