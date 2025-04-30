package com.example.sidedemo.calendar.cache.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertDto {
    private Long id;
    private Long userId;
    private String planName;
    private String planContent;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String location;
    private LocalTime alarmTime;

    // 반복 일정
    private String planType;            // SINGLE, RECURRING
    private String repeatUnit;          // WEEKLY, MONTHLY, YEARLY
    private Integer repeatInterval;
    private Integer repeatDayOfMonth;
    private Integer repeatWeek;
    private DayOfWeek repeatWeekday;
    private Set<LocalDate> exceptionDates;
}