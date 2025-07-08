package com.example.sidedemo.calendar.plan.controller;

import com.example.sidedemo.calendar.plan.dto.read.request.ReadDailyRequest;
import com.example.sidedemo.calendar.plan.dto.read.request.ReadMonthlyRequest;
import com.example.sidedemo.calendar.plan.dto.read.request.ReadSingleRequest;
import com.example.sidedemo.calendar.plan.dto.write.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.write.DeleteRequest;
import com.example.sidedemo.calendar.plan.dto.write.Response;
import com.example.sidedemo.calendar.plan.dto.write.UpdateRequest;
import com.example.sidedemo.calendar.plan.service.PlanService;
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

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<Response> createPlan(
            @AuthenticationPrincipal String userId,
            @RequestBody @Valid CreateRequest request
    ) {
        Response created = planService.createPlan(request, Long.valueOf(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updatePlan(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateRequest request
    ) {
        request.setId(id);
        Response updated = planService.updatePlan(request, Long.valueOf(userId));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deletePlan(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id
    ) {
        DeleteRequest deleteReq = DeleteRequest.builder().id(id).build();
        Response deleted = planService.deletePlan(deleteReq, Long.valueOf(userId));
        return ResponseEntity.ok(deleted);
    }

    @DeleteMapping("/{id}/occurrence")
    public ResponseEntity<Response> deleteOccurrence(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Response resp = planService.deleteOccurrence(id, date, Long.valueOf(userId));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/month")
    public ResponseEntity<List<Response>> getMonthlyPlans(
            @AuthenticationPrincipal String userId,
            @Valid ReadMonthlyRequest req
    ) {
        YearMonth ym = YearMonth.of(req.getYear(), req.getMonth());
        List<Response> list = planService.readMonthlyPlans(Long.valueOf(userId), ym);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Response>> getDailyPlans(
            @AuthenticationPrincipal String userId,
            @Valid ReadDailyRequest req
    ) {
        List<Response> list = planService.readDailyPlans(Long.valueOf(userId), req.getDate());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getPlan(
            @AuthenticationPrincipal String userId,
            @Valid ReadSingleRequest req
    ) {
        Response plan = planService.readPlan(Long.valueOf(userId), req.getPlanId());
        return ResponseEntity.ok(plan);
    }
}