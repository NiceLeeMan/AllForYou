package com.example.sidedemo.calendar.plan.dto.read;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import com.example.sidedemo.enums.Enums.*;
import lombok.*;

/**
 * 3) 단일 상세 조회 (plan_id) → 전체 필드
 */
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SingleResponse {
    private Long id;
    private Long userId;
    private String planName;
    private String planContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private LocalTime alarmTime;
    private PlanType planType;
    private RepeatUnit repeatUnit;
    private Integer repeatInterval;
    private Integer repeatDayOfMonth;
    private DayOfWeek repeatWeekday;
    private Set<LocalDate> exceptionDates;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}