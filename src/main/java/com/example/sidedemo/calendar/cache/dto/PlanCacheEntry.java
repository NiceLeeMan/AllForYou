package com.example.sidedemo.calendar.cache.dto;


import com.example.sidedemo.calendar.plan.dto.write.Response;
import com.example.sidedemo.enums.Enums.*;
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
    private PlanType planType;
    private RepeatUnit repeatUnit;
    private Integer repeatInterval;
    private Integer repeatDayOfMonth;
    private Set<DayOfWeek> repeatWeekdays;
    private Set<LocalDate> exceptionDates;

    //언제 만들어지고 업데이트됐는지.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    // Factory for summary
    public static PlanCacheEntry monthlyPlansOverview(Response dto, YearMonth ym) {

        //시작(2025-03-28), 종료(2025-04-03)
        LocalDate start = dto.getStartDate(); //시작 2025-03-28,
        LocalDate end = dto.getEndDate(); //종료 2025-04-03

        //해당 MM의 1일~말일
        LocalDate firstOfMonth = ym.atDay(1); // 2025-03-01
        LocalDate lastOfMonth = ym.atEndOfMonth(); // 2025-03-31

        //plan 시작일이 전번 달 이면 segStart = 1 , 1일이후면 segStart = startDate
        LocalDate segStart = start.isBefore(firstOfMonth) ? firstOfMonth : start;

        //plan 종료일이 다음달 말일이면 segStart = lastOfMonth , 1일이후면 segStart = endDate
        LocalDate segEnd = end.isAfter(lastOfMonth) ? lastOfMonth : end;

        return PlanCacheEntry.builder()
                .planId(dto.getId())
                .planName(dto.getPlanName())
                .segStart(segStart)
                .segEnd(segEnd)
                .build();
    }

}