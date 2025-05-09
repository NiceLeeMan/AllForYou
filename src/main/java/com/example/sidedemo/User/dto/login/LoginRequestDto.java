package com.example.sidedemo.User.dto.login;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {

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
}