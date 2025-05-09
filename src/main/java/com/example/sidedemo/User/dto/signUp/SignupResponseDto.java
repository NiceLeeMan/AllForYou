package com.example.sidedemo.User.dto.signUp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignupResponseDto {

    @Schema(description = "응답 상태 코드", example = "200")
    private SignupResponseCode code;

    @Schema(description = "결과 메시지", example = "회원가입이 정상 처리되었습니다.")
    private String message;

    @Schema(description = "생성된 사용자 아이디", example = "Awzer123")
    private String userId;

    @Schema(description = "응답 생성 시각", example = "2025-05-08T15:30:00")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}