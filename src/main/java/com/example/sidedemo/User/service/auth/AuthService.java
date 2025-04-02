package com.example.sidedemo.User.service.auth;


import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.User.dto.login.LoginRequestDto;
import com.example.sidedemo.User.dto.login.LoginResponseDto;
import com.example.sidedemo.User.dto.signUp.SignupRequestDto;
import com.example.sidedemo.User.dto.signUp.SignupResponseDto;
import com.example.sidedemo.User.repository.UserRepository;
import com.example.sidedemo.User.service.notifiaction.EmailService;
import com.example.sidedemo.User.store.VerificationCodeStore;
import com.example.sidedemo.User.util.JwtUtil;
import com.example.sidedemo.User.util.VerificationCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeGenerator codeGenerator;
    private final VerificationCodeStore codeStore;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    /*  함수
    1. usrename, password , email, phone_number를 입력받는다
    2. username, password phonenumber은 다른사람과 겹쳐서는 안되며 username == password여도 안된다.
    3. 입력이 끝난뒤 확인버튼을 누르면 이메일로 인증번호가 전송된다.
    4. 인증번호가 올바르면 회원가입처리가 된다. / 인증번호가 잘못되면 회원가입을 거부하고 오류메시지를 전달한다
    5. 회원 테이블에 회원정보가 저장되며, 해쉬화등 보안적인 요소를 다 고려해서 저장한다(username, password, eamil, phonenumber)
    6. 요청간에는 dto를 사용한다
     */

    public void sendVerificationCode(String email) {
        String code = codeGenerator.generate6DigitCode(); // 4자리
        codeStore.saveCode(email, code);
        emailService.sendAuthCodeMail(email,"인증번호 전송",code);
    }

    public SignupResponseDto registerUser(SignupRequestDto req) {

        //중복검사 코드작성 필요

        String storedCode = codeStore.getCode(req.getEmail());
        if (storedCode == null || !storedCode.equals(req.getVerificationCode())) {
            System.out.println("인증코드 틀렸다");
            throw new RuntimeException("Invalid verification code");
        }

        // 해시화 후 저장
        User user = User.builder()
                .name(req.getName())
                .userId(req.getUserId())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .build();

        userRepository.save(user);
        // 사용 후 코드 삭제
        codeStore.removeCode(req.getEmail());

        return SignupResponseDto.builder()
                .message("Registration success!")
                .userId(user.getUserId())
                .build();
    }

    /* 로그인 함수
     */

    public LoginResponseDto login(LoginRequestDto request) {

        // 1. 사용자 조회
        Optional<User> userOpt = userRepository.findByUserId(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 3. JWT 토큰 생성 (예: subject로 userId 사용)
        String token = jwtUtil.generateToken(user.getUserId());

        // 4. 응답 DTO 생성
        return LoginResponseDto.builder()
                .message("Login success")
                .token(token)
                .build();
    }

}
