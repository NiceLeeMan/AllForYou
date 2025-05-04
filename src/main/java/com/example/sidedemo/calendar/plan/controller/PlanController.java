package com.example.sidedemo.calendar.plan.controller;



import com.example.sidedemo.calendar.plan.service.PlanServiceImpl;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor

public class PlanController {

    private final PlanServiceImpl planService;







}