package com.example.sidedemo.User.dto.signUp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SignupResponseDto {
    private String message;
    private String userId;
}