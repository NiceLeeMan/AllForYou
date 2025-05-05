package com.example.sidedemo.calendar.plan.controller;



import com.example.sidedemo.calendar.plan.dto.read.ReadDailyRequest;
import com.example.sidedemo.calendar.plan.dto.read.ReadMonthlyRequest;
import com.example.sidedemo.calendar.plan.dto.read.ReadSingleRequest;
import com.example.sidedemo.calendar.plan.dto.write.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.write.DeleteRequest;
import com.example.sidedemo.calendar.plan.dto.write.Response;
import com.example.sidedemo.calendar.plan.dto.write.UpdateRequest;
import com.example.sidedemo.calendar.plan.service.PlanServiceImpl;

import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor

public class PlanController {

    private final PlanServiceImpl planService;


    /**
     * 1) 계획 생성
     * POST /api/plans
     */
    @PostMapping
    public ResponseEntity<Response> createPlan(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody @Valid CreateRequest request
    ) {
        Response created = planService.createPlan(request, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    /**
     * 2) 계획 수정
     * PUT /api/plans/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePlan(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateRequest request
    ) {
        // PathVariable 과 Request body 의 id 필드를 일치시킵니다.
        request.setId(id);
        Response updated = planService.updatePlan(request, userId);
        return ResponseEntity
                .ok(updated);
    }

    /**
     * 3) 계획 삭제
     * DELETE /api/plans/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePlan(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id
    ) {
        DeleteRequest deleteReq = DeleteRequest.builder()
                .id(id)
                .build();
        Response deleted = planService.deletePlan(deleteReq, userId);
        return ResponseEntity
                .ok(deleted);
    }

    @GetMapping("/month")
    public ResponseEntity<List<Response>> getMonthlyPlans(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid ReadMonthlyRequest req
    ) {
        YearMonth ym = YearMonth.of(req.getYear(), req.getMonth());
        List<Response> list = planService.readMonthlyPlans(userId, ym);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Response>> getDailyPlans(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid ReadDailyRequest req
    ) {
        List<Response> list = planService.readDailyPlans(userId, req.getDate());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPlan(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid ReadSingleRequest req
    ) {
        // or simply @PathVariable Long id; here we bind via DTO
        Response plan = planService.readPlan(userId, req.getPlanId());
        return ResponseEntity.ok(plan);
    }


}