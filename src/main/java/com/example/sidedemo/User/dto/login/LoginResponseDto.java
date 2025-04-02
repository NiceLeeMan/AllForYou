package com.example.sidedemo.User.dto.login;






import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String message;
    private String token; // JWT 토큰
}