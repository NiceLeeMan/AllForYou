package com.example.sidedemo.User.dto.signUp;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {

    @NotBlank(message = "UserName is required")
    @NotNull
    private String name;

    @NotBlank(message = "User ID is required")
    @NotNull
    private String userId;

    @NotBlank(message = "Password is required")
    @NotNull
    private String password;

    @NotBlank(message = "Email is required")
    @NotNull
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @NotNull
    private String phoneNumber;

    // 인증번호(이메일로 전송된 코드)
    @NotBlank(message = "Verification code is required")
    @NotNull
    private String verificationCode;
}
