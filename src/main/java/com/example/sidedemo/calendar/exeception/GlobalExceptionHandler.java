package com.example.sidedemo.calendar.exeception;

import com.example.sidedemo.calendar.plan.exception.PlanNotFoundException;
import com.example.sidedemo.calendar.plan.exception.UnauthorizedPlanAccessException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlanNotFound(PlanNotFoundException ex) {
        log.error("PlanNotFoundException 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("PLAN_NOT_FOUND")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UnauthorizedPlanAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedPlanAccessException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .code("UNAUTHORIZED")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockException ex) {
        log.error("동시성 예외 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("CONCURRENT_MODIFICATION")
                .message("다른 사용자가 먼저 수정했습니다. 새로고침 후 다시 시도하세요.")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("일반 예외 발생", ex);
        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .message("서버 내부 오류가 발생했습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
