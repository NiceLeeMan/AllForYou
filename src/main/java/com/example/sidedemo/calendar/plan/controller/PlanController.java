package com.example.sidedemo.calendar.plan.controller;


import com.example.sidedemo.domain.User;
import com.example.sidedemo.calendar.plan.service.PlanService;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor

public class PlanController {
    private final PlanService planService;

    /**
     * 계획 추가 API
     * 실제 운영 환경에서는 보안 컨텍스트에서 현재 로그인한 사용자(User)를 추출합니다.
     * 여기서는 예시로 쿼리 파라미터로 userId를 전달받아 더미 User 객체를 생성합니다.
     */



}