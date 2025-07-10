package com.example.sidedemo.calendar.exception;

import com.example.sidedemo.calendar.plan.dto.request.CreateRequest;
import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // TestController 와 GlobalExceptionHandler 만 등록
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /** 1) DTO 검증 실패 → 400 + VALIDATION_ERROR */
    @Test
    void handleValidation() throws Exception {
        mockMvc.perform(post("/test/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))  // CreateRequest 의 @NotNull 필드가 비어 있음
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.errors").isArray());
    }



    /** 3) DB 연결 오류 → 503 + DB_CONNECTION_ERROR */
    @Test
    void handleDbConnectionFail() throws Exception {
        mockMvc.perform(get("/test/db-conn"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("DB_CONNECTION_ERROR"));
    }

    /** 7) Redis 연결 실패 → 503 + CACHE_CONNECTION_ERROR */
    @Test
    void handleRedisConnFail() throws Exception {
        mockMvc.perform(get("/test/redis-conn"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.code").value("CACHE_CONNECTION_ERROR"));
    }

    /** 8) 기타 예기치 않은 예외 → 500 + INTERNAL_ERROR */
    @Test
    void handleAll() throws Exception {
        mockMvc.perform(get("/test/unknown"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"));
    }

    // —————— 테스트 전용 컨트롤러 ——————
    @RestController
    static class TestController {
        @PostMapping("/test/validate")
        public void validate(@Valid @RequestBody CreateRequest dto) { }

        @GetMapping("/test/db-no-space")
        public void dbNoSpace() {
            throw new DataAccessResourceFailureException("no space");
        }

        @GetMapping("/test/db-conn")
        public void dbConnFail() {
            throw new CannotGetJdbcConnectionException("cannot connect");
        }

        @GetMapping("/test/db-integrity")
        public void dbIntegrity() {
            throw new DataIntegrityViolationException("constraint");
        }

        @GetMapping("/test/jpa")
        public void jpaError() {
            throw new PersistenceException("jpa");
        }

        @GetMapping("/test/redis-oom")
        public void redisOom() {
            throw new RedisSystemException("redis", new RuntimeException("OOM"));
        }

        @GetMapping("/test/redis-conn")
        public void redisConnFail() {
            throw new RedisConnectionFailureException("redis");
        }

        @GetMapping("/test/unknown")
        public void unknown() {
            throw new RuntimeException("unknown");
        }
    }
}