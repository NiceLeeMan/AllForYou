package com.example.sidedemo.User.dto.signUp;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    @NotBlank(message = "UserName is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하로 입력해주세요.")
    @Pattern(
            regexp = "^[\\p{Alnum}\\p{Punct}]+$",
            message = "아이디는 영문·숫자·특수문자를 조합하여 입력해주세요."
    )
    private String userId;


    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하로 입력해주세요.")
    // 최소 1대문자, 1소문자, 1숫자, 1특수문자 포함
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\p{Punct}).+$",
            message = "비밀번호는 대문자·소문자·숫자·특수문자를 모두 포함해야 합니다."
    )
    private String password;


    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @Size(max = 30, message = "이메일은 100자 이하로 입력해주세요.")
    private String email;

    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "휴대폰 번호는 10~11자리 숫자만 입력 가능합니다."
    )
    private String phoneNumber;

    // 인증번호(이메일로 전송된 코드)
    @NotBlank(message = "인증 코드는 필수 입력 값입니다.")
    @Size(min = 6, max = 6, message = "인증 코드는 6자리 숫자로 입력해주세요.")
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "인증 코드는 숫자만 입력 가능합니다."
    )
    private String verificationCode;
}
