package com.example.sidedemo.calendar.plan.dto.write;


import com.example.sidedemo.enums.Enums.PlanType;
import com.example.sidedemo.enums.Enums.RepeatUnit;
import lombok.*;
import java.time.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanWriteResponse {
    private Long id;
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
    private Integer repeatWeek;
    private DayOfWeek repeatWeekday;
    private Set<LocalDate> exceptionDates;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}