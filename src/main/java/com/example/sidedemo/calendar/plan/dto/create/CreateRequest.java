package com.example.sidedemo.calendar.plan.dto.create;


import com.example.sidedemo.enums.Enums.PlanType;
import com.example.sidedemo.enums.Enums.RepeatUnit;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Set;
import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateRequest {

    // 내용은 선택적으로 입력받되, 값이 없으면 기본값("") 처리 가능

    private String planName;          // 계획 제목 (기본값 "제목없음"은 서비스에서 처리 가능)- 이부분은 UI에서 처리해도 되나?

    // 내용은 선택적으로 입력받되, 값이 없으면 기본값("") 처리 가능
    private String planContent;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;      // 시작 날짜 (반드시 입력)

    @NotNull(message = "End date is required")
    private LocalDate endDate;        // 종료 날짜 (반복일 경우 반복 종료일)

    @NotNull(message = "Start time is required")
    private LocalTime startTime;      // 시작 시간

    @NotNull(message = "End time is required")
    private LocalTime endTime;        // 종료 시간

    // 선택 입력
    private String location;          // 위치
    private LocalTime alarmTime;      // 알람 시간

    @NotNull(message = "Plan type is required")
    private PlanType planType;        // 계획 유형: SINGLE 또는 RECURRING

    // 반복 계획에 해당하는 필드 (planType이 RECURRING인 경우 사용)
    // 반복 단위: WEEKLY, MONTHLY, YEARLY
    private RepeatUnit repeatUnit;

    // 주 단위 반복: 몇 주 간격으로 반복되는지 (예: 1 = 매주, 2 = 2주마다)
    private Integer repeatInterval;

    // 월 단위 반복 (일 단위 반복): 매월 N일에 반복되는 경우
    private Integer repeatDayOfMonth;

    // 월 단위 반복 (주/요일 단위 반복): 몇 번째 주인지 (예: 둘째 주 → 2)
    private Integer repeatWeek;

    // 월 단위 반복 (주/요일 단위 반복): 해당 요일 (예: WEDNESDAY)
    private DayOfWeek repeatWeekday;

    // 예외 날짜: 반복 계획 중 제외할 날짜들
    private Set<LocalDate> exceptionDates;
}