package com.example.sidedemo.calendar.exception;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    /**
     * 에러 코드 (예: "VALIDATION_ERROR", "DB_CONNECTION_ERROR" 등)
     */
    private String code;

    /**
     * 사용자에게 보여줄 메시지
     */
    private String message;

    /**
     * 상세 에러 정보 (Validation 에러의 경우 필드별 에러 메시지)
     */
    private Map<String, String> details;
}