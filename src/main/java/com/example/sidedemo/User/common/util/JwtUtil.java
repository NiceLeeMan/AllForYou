package com.example.sidedemo.User.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // 실제 서비스에서는 이 키를 안전하게 보관해야 합니다. (예: 환경 변수 또는 Vault 사용)
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMillis;

    /**
     * SecretKey를 생성한다.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

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
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰의 유효성을 검증한다.
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // JWT 관련 모든 예외를 처리
            return false;
        } catch (IllegalArgumentException e) {
            // JWT claims string이 비어있음
            return false;
        }
    }

    /**
     * JWT 토큰에서 userId를 추출한다.
     * @param token JWT 토큰
     * @return 토큰에서 추출한 userId
     * @throws JwtException 토큰이 유효하지 않거나 만료된 경우
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    /**
     * JWT 토큰의 만료 시간을 확인한다.
     * @param token JWT 토큰
     * @return 토큰이 만료되었으면 true, 그렇지 않으면 false
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true; // 파싱 실패 시 만료된 것으로 간주
        }
    }

    /**
     * JWT 토큰의 만료 시간까지 남은 시간을 밀리초로 반환한다.
     * @param token JWT 토큰
     * @return 만료까지 남은 시간 (밀리초), 토큰이 유효하지 않으면 -1
     */
    public long getTimeUntilExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            Date now = new Date();
            return expiration.getTime() - now.getTime();
        } catch (Exception e) {
            return -1;
        }
    }
}
