package com.example.sidedemo.calendar.execption;


import lombok.*;

import java.util.List;

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
}