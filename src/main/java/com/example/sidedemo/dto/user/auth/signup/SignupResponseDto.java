package com.example.sidedemo.dto.user.auth.signup;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SignupResponseDto {
    private String message;
    private String userId;
}