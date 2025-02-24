package com.example.sidedemo.dto.user.auth.login;






import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String message;
    private String token; // JWT 토큰
}