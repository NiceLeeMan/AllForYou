package com.example.sidedemo.controller.auth;


import com.example.sidedemo.dto.user.auth.signup.SignupRequestDto;
import com.example.sidedemo.dto.user.auth.signup.SignupResponseDto;
import com.example.sidedemo.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SignUpController {

    private final AuthService authService;



    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        authService.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent to: " + email);
    }

    /**
     * 2) 회원가입 처리 API
     *    - 클라이언트에서 인증코드, 이름, 아이디, 비밀번호, 이메일, 전화번호 등을 담아서 요청
     *    - 인증코드가 맞을 경우 회원가입 DB 저장
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signUp(
            @Validated @RequestBody SignupRequestDto request) {
        // @Validated: DTO 내에 @NotBlank 등 Bean Validation 적용
        SignupResponseDto response = authService.registerUser(request);
        System.out.println("회원가입 API 호출됨");
        return ResponseEntity.ok(response);
    }

}
