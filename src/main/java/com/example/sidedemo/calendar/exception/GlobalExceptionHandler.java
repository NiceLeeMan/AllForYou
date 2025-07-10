package com.example.sidedemo.calendar.exception;

import com.example.sidedemo.calendar.plan.exception.PlanNotFoundException;
import com.example.sidedemo.calendar.plan.exception.UnauthorizedPlanAccessException;
import com.example.sidedemo.User.common.exception.BadRequestException;
import com.example.sidedemo.User.common.exception.DuplicateEmailException;
import com.example.sidedemo.User.common.exception.DuplicateUserIdException;
import com.example.sidedemo.User.common.exception.InvalidVerificationCodeException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Validation 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.error("Validation 예외 발생: {}", errors);
        ErrorResponse error = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("입력 데이터가 유효하지 않습니다.")
                .details(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint Violation 예외 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("VALIDATION_ERROR")
                .message("입력 데이터가 유효하지 않습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // User 모듈 예외 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        log.error("BadRequestException 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        log.error("DuplicateEmailException 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("DUPLICATE_EMAIL")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DuplicateUserIdException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserId(DuplicateUserIdException ex) {
        log.error("DuplicateUserIdException 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("DUPLICATE_USER_ID")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidVerificationCode(InvalidVerificationCodeException ex) {
        log.error("InvalidVerificationCodeException 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .code("INVALID_VERIFICATION_CODE")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Calendar 모듈 예외 처리
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
