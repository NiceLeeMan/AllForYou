package com.example.sidedemo.calendar.cache.dto;


import com.example.sidedemo.calendar.plan.dto.write.PlanWriteResponse;
import com.example.sidedemo.enums.Enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;

import java.lang.reflect.Type;
import java.time.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCacheEntry {
    // 공통
    private Long planId;
    private String planName;

    // 요약 -> 월별 인덱스용
    private LocalDate segStart;
    private LocalDate segEnd;

    // 상세 -> 세부 조회용
    private String planContent;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String location;
    private LocalTime alarmTime;

    // 반복 정보
    private PlanType planType;
    private RepeatUnit repeatUnit;
    private Integer repeatInterval;
    private Integer repeatDayOfMonth;
    private Integer repeatWeek;
    private DayOfWeek repeatWeekday;
    private Set<LocalDate> exceptionDates;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    // Factory for summary
    public static PlanCacheEntry fromSummary(PlanWriteResponse dto, YearMonth ym) {
        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();
        LocalDate firstOfMonth = ym.atDay(1);
        LocalDate lastOfMonth = ym.atEndOfMonth();
        LocalDate segStart = start.isBefore(firstOfMonth) ? firstOfMonth : start;
        LocalDate segEnd = end.isAfter(lastOfMonth) ? lastOfMonth : end;

        return PlanCacheEntry.builder()
                .planId(dto.getId())
                .planName(dto.getPlanName())
                .segStart(segStart)
                .segEnd(segEnd)
                .build();
    }

}