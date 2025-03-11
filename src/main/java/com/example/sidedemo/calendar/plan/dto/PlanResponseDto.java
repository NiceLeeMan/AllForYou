package com.example.sidedemo.calendar.plan.dto;



import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlanResponseDto {

    private Long id; //plan_id값을 의미
    private String planName;
    private String planContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private LocalTime alarmTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String planType; // "SINGLE" 또는 "RECURRING"
}