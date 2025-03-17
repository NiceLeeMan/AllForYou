package com.example.sidedemo.calendar.plan.controller;


import com.example.sidedemo.domain.User;
import com.example.sidedemo.calendar.plan.service.PlanService;
import com.example.sidedemo.calendar.plan.dto.create.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.create.CreateResponse;

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

    @PostMapping("/{userId}")
    public ResponseEntity<CreateResponse> createPlan(
            @Validated @RequestBody CreateRequest request,
            @RequestParam Long userId) {
        // 실제 서비스에서는 SecurityContextHolder를 통해 로그인한 User 객체를 가져옵니다.
        User dummyUser = new User();
        dummyUser.setId(userId);

        CreateResponse response = planService.createPlan(request, dummyUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<CreateResponse> updatePlan(
            @PathVariable Long planId,
            @Validated @RequestBody CreateRequest request) {
        CreateResponse response = planService.updatePlan(planId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.ok("Plan deleted successfully");
    }
}