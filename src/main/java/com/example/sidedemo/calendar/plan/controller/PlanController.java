package com.example.sidedemo.calendar.plan.controller;

import com.example.sidedemo.calendar.plan.dto.request.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.request.ReadDailyRequest;
import com.example.sidedemo.calendar.plan.dto.request.ReadSingleRequest;
import com.example.sidedemo.calendar.plan.dto.request.ReadMonthlyRequest;
import com.example.sidedemo.calendar.plan.dto.request.DeleteRequest;
import com.example.sidedemo.calendar.plan.dto.request.updateRequest;
import com.example.sidedemo.calendar.plan.dto.response.Response;
import com.example.sidedemo.calendar.plan.service.PlanCommandService;
import com.example.sidedemo.calendar.plan.service.PlanQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanCommandService planCommandService;
    private final PlanQueryService planQueryService;

    @PostMapping
    public ResponseEntity<Response> createPlan(
            @AuthenticationPrincipal String userId,
            @RequestBody @Valid CreateRequest request
    ) {
        Response created = planCommandService.createPlan(request, Long.valueOf(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePlan(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @RequestBody @Valid updateRequest request
    ) {
        request.setId(id);
        Response updated = planCommandService.updatePlan(request, Long.valueOf(userId));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePlan(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id
    ) {
        DeleteRequest deleteReq = DeleteRequest.builder().id(id).build();
        Response deleted = planCommandService.deletePlan(deleteReq, Long.valueOf(userId));
        return ResponseEntity.ok(deleted);
    }

    @DeleteMapping("/{id}/occurrence")
    public ResponseEntity<Response> deleteOccurrence(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Response resp = planCommandService.deleteOccurrence(id, date, Long.valueOf(userId));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/month")
    public ResponseEntity<List<Response>> getMonthlyPlans(
            @AuthenticationPrincipal String userId,
            @Valid ReadMonthlyRequest req
    ) {
        YearMonth ym = YearMonth.of(req.getYear(), req.getMonth());
        List<Response> list = planQueryService.readMonthlyPlans(Long.valueOf(userId), ym);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Response>> getDailyPlans(
            @AuthenticationPrincipal String userId,
            @Valid ReadDailyRequest req
    ) {
        List<Response> list = planQueryService.readDailyPlans(Long.valueOf(userId), req.getDate());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPlan(
            @AuthenticationPrincipal String userId,
            @Valid ReadSingleRequest req
    ) {
        Response plan = planQueryService.readPlan(Long.valueOf(userId), req.getPlanId());
        return ResponseEntity.ok(plan);
    }
}