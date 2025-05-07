package com.example.sidedemo.calendar.plan.dto.write;


import com.example.sidedemo.calendar.plan.enums.Enums.PlanType;
import com.example.sidedemo.calendar.plan.enums.Enums.RepeatUnit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private Long id;
    private String planName;
    private String planContent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String location;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime alarmTime;

    private PlanType planType;
    private RepeatUnit repeatUnit;
    private Integer repeatInterval;

    @JsonFormat // class-level NON_NULL 적용으로 null이면 응답에서 제외
    private Integer repeatDayOfMonth;

    private Set<DayOfWeek> repeatWeekdays;
    private Set<LocalDate> exceptionDates;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long userId;
    private String message;
}
