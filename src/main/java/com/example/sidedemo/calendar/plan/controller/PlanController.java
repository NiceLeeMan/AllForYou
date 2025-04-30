package com.example.sidedemo.calendar.plan.controller;


import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.calendar.plan.dto.create.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.create.CreateResponse;
import com.example.sidedemo.calendar.plan.service.impl.PlanServiceImpl;

import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor

public class PlanController {

    private final PlanServiceImpl planService;

    @PostMapping
    public ResponseEntity<CreateResponse> createPlan(
            @Validated @RequestBody CreateRequest request,
            @RequestParam Long userId) {
        // 실제 환경에서는 SecurityContext를 통해 인증된 User 객체를 가져와야 합니다.
        // 여기서는 단순하게 userId로 User 객체를 생성합니다.
        User user = new User();
        user.setId(userId);



    }



}