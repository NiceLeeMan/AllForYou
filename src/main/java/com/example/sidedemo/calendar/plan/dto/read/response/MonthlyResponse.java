package com.example.sidedemo.calendar.plan.dto.read.response;


import lombok.*;

import java.time.LocalDate;


/**
 * 1) 한 달치 조회 (YYYY-MM) → planName, startDate, endDate
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonthlyResponse {
    private Long planId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
}