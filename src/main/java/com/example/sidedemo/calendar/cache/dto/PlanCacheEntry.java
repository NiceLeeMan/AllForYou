package com.example.sidedemo.calendar.cache.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;

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
    private String planType;
    private String repeatUnit;
    private Integer repeatInterval;
    private Integer repeatDayOfMonth;
    private Integer repeatWeek;
    private DayOfWeek repeatWeekday;
    private Set<LocalDate> exceptionDates;


    // Factory for summary
    public static PlanCacheEntry fromSummary(UpsertDto dto, YearMonth ym) {
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

    // Factory for detail
    public static PlanCacheEntry fromDetail(UpsertDto dto) {
        return PlanCacheEntry.builder()
                .planId(dto.getId())
                .planName(dto.getPlanName())
                .planContent(dto.getPlanContent())
                .startDate(dto.getStartDate())
                .startTime(dto.getStartTime())
                .endDate(dto.getEndDate())
                .endTime(dto.getEndTime())
                .location(dto.getLocation())
                .alarmTime(dto.getAlarmTime())
                .planType(dto.getPlanType())
                .repeatUnit(dto.getRepeatUnit())
                .repeatInterval(dto.getRepeatInterval())
                .repeatDayOfMonth(dto.getRepeatDayOfMonth())
                .repeatWeek(dto.getRepeatWeek())
                .repeatWeekday(dto.getRepeatWeekday())
                .exceptionDates(dto.getExceptionDates())
                .build();

    }

}