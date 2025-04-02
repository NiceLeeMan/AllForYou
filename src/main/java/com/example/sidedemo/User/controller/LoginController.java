package com.example.sidedemo.User.controller;




import com.example.sidedemo.User.dto.login.LoginRequestDto;
import com.example.sidedemo.User.dto.login.LoginResponseDto;
import com.example.sidedemo.User.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    /**
     * 로그인 API
     *
     * 1. 클라이언트로부터 LoginRequestDto를 받아서 AuthService의 login()을 호출합니다.
     * 2. AuthService가 사용자 인증에 성공하면, JWT 토큰이 포함된 LoginResponseDto를 반환합니다.
     * 3. 생성된 JWT 토큰을 HttpOnly 쿠키에 저장하여 클라이언트로 전송합니다.
     * 4. 응답 본문에도 로그인 성공 메시지와 토큰(필요 시)을 담아 반환합니다.
     *
     * @param request 로그인 요청 데이터 (userId, password)
     * @param response HttpServletResponse, 쿠키 설정을 위해 사용
     * @return 로그인 성공 시 LoginResponseDto 반환
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Validated @RequestBody LoginRequestDto request,
            HttpServletResponse response) {

        // 1. 로그인 처리 (사용자 인증 및 JWT 토큰 생성)
        LoginResponseDto loginResponse = authService.login(request);

        // 2. JWT 토큰을 HTTP‑Only 쿠키에 저장
        Cookie tokenCookie = new Cookie("token", loginResponse.getToken());
        tokenCookie.setHttpOnly(true);           // JavaScript 접근 방지 (XSS 방어)
        tokenCookie.setPath("/");                  // 모든 경로에서 접근 가능하도록 설정


        tokenCookie.setMaxAge((int)(jwtExpirationInSeconds()));         // 예: 10시간 동안 유효
        response.addCookie(tokenCookie);

        // 3. 클라이언트에게 응답 반환
        return ResponseEntity.ok(loginResponse);
    }

    // JwtUtil에서 설정한 만료 시간을 초 단위로 변환하는 헬퍼 메서드 (예시)
    private long jwtExpirationInSeconds() {
        // 예: 10시간 → 36000초
        return 36000;
    }
}
