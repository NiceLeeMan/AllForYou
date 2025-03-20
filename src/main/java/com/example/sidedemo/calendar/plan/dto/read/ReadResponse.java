package com.example.sidedemo.calendar.plan.dto.read;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReadResponse {
    private Long planId;
    private String planName;
    private String planContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private LocalDateTime alarmTime;

    // 반복 계획 전용 속성 (단일 계획인 경우 null)
    private String repeatType;
    private String repeatDetail;
    private Integer repeatInterval;
}
