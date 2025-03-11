package com.example.sidedemo.calendar.plan.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PlanRequestDto {

    @NotBlank(message = "일정 이름은 필수입니다.")
    private String planName;

    // 내용은 선택적일 수 있으므로 검증은 생략
    private String planContent;

    @NotNull(message = "시작 날짜는 필수입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    private LocalDate endDate;

    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalTime startTime;

    @NotNull(message = "종료 시간은 필수입니다.")
    private LocalTime endTime;

    // location, alarmTime는 선택 사항으로 처리 가능
    private String location;
    private LocalTime alarmTime;

    @NotBlank(message = "일정 유형은 필수입니다. (SINGLE 또는 RECURRING)")
    private String planType;
}