package com.example.sidedemo.calendar.plan.dto.write;


import com.example.sidedemo.enums.Enums.RepeatUnit;
import com.example.sidedemo.enums.Enums.PlanType;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequest {

    @NotNull(message = "Plan ID is required")
    private Long id;                    // 수정할 계획의 식별자


    //수정 할 경우에만 값을 채워서 보내면 됨. 반드시 보내야하는게 아니기에 NotNull, NotBlank 필요없음
    private String planName;            // 변경할 계획 제목
    private String planContent;         // 변경할 계획 내용
    private LocalDate startDate;        // 변경할 시작 날짜
    private LocalDate endDate;          // 변경할 종료 날짜 (반복 종료일 포함)
    private LocalTime startTime;        // 변경할 시작 시간
    private LocalTime endTime;          // 변경할 종료 시간
    private String location;            // 변경할 위치
    private LocalTime alarmTime;        // 변경할 알람 시간
    private PlanType planType;          // 변경할 계획 유형

    // 반복 계획 관련 필드 (planType이 RECURRING인 경우 사용)
    private RepeatUnit repeatUnit;
    private Integer repeatInterval;
    private Integer repeatDayOfMonth;
    private Set<DayOfWeek> repeatWeekdays;
    private Set<LocalDate> exceptionDates;  // 변경할 예외 날짜들
}