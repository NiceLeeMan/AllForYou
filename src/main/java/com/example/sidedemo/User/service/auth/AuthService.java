package com.example.sidedemo.User.service.auth;


import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.User.dto.login.LoginRequestDto;
import com.example.sidedemo.User.dto.login.LoginResponseDto;
import com.example.sidedemo.User.dto.signUp.SignupRequestDto;
import com.example.sidedemo.User.dto.signUp.SignupResponseDto;
import com.example.sidedemo.User.exception.*;
import com.example.sidedemo.User.repository.UserRepository;
import com.example.sidedemo.User.service.notifiaction.EmailService;
import com.example.sidedemo.User.store.VerificationCodeStore;
import com.example.sidedemo.User.util.JwtUtil;
import com.example.sidedemo.User.util.VerificationCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    public SignupResponseDto registerUser(SignupRequestDto req) {


        try {
            //1)인증 코드 확인
            verifyCode(req.getEmail(), req.getVerificationCode());
            //2)ID , PW , Email 검사
            validateDistinctUserIdAndPassword(req.getUserId(),req.getPassword());
            checkDuplicates(req.getUserId(),req.getPassword());

            User user = User.builder()
                    .name(req.getName())
                    .userId(req.getUserId())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .email(req.getEmail())
                    .phoneNumber(req.getPhoneNumber())
                    .build();

            userRepository.save(user);

            return (
                    SignupResponseDto.builder()
                    .message("회원가입이 정상 처리되었습니다.")
                    .userId(req.getUserId())
                    .build())
                    ;
        } catch (DataIntegrityViolationException e) {
            log.error("Database integrity violation during registerUser", e);
            throw new BadRequestException("회원 정보 저장 중 오류가 발생했습니다.");
        } finally {
            codeStore.removeCode(req.getEmail());
        }
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

    private void checkDuplicates(String userId, String email) {
        if (userRepository.existsUserByUserId(userId)) {
            log.warn("Duplicate userId detected: {}", userId);
            throw new DuplicateUserIdException("이미 사용 중인 아이디입니다.");
        }

        if (userRepository.existsUserByEmail(email)) {
            log.warn("Duplicate email detected: {}", email);
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }
    }

    private void verifyCode(String email, String code) {
        String stored = codeStore.getCode(email);
        byte[] storedBytes = stored != null ? stored.getBytes(StandardCharsets.UTF_8) : new byte[0];
        byte[] inputBytes = code.getBytes(StandardCharsets.UTF_8);
        if (stored == null || !MessageDigest.isEqual(storedBytes, inputBytes)) {
            log.warn("Invalid verification code for email={}", email);
            throw new InvalidVerificationCodeException("인증코드가 유효하지 않습니다.");
        }
    }

    private void validateDistinctUserIdAndPassword(String userId, String password)  {
        if (userId.equals(password)) {
            log.warn("UserId and password must differ: {}", userId);
            throw new BadRequestException("아이디와 비밀번호는 같을 수 없습니다.");
        }
    }

}
