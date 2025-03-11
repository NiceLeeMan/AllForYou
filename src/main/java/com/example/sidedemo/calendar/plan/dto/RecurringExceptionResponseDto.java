package com.example.sidedemo.calendar.plan.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringExceptionResponseDto {
    private Long id;
    private Long recurringPlanId;
    private LocalDate exceptionDate;
    private boolean isCanceled;
    private LocalDate newDate;
}