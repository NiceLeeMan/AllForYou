package com.example.sidedemo.calendar.plan.dto.write;


import com.example.sidedemo.calendar.plan.enums.Enums.RepeatUnit;
import com.example.sidedemo.calendar.plan.enums.Enums.PlanType;
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

    // Optional fields: if present, must satisfy size constraints
    @Size(min = 1, max = 100, message = "Plan name must be between 1 and 100 characters")
    private String planName;            // 변경할 계획 제목

    @Size(max = 500, message = "Plan content must be at most 500 characters")
    private String planContent;         // 변경할 계획 내용

    private LocalDate startDate;        // 변경할 시작 날짜
    private LocalDate endDate;          // 변경할 종료 날짜 (반복 종료일 포함)

    private LocalTime startTime;        // 변경할 시작 시간
    private LocalTime endTime;          // 변경할 종료 시간

    @Size(min = 1, max = 200, message = "Location must be between 1 and 200 characters")
    private String location;            // 변경할 위치

    private LocalTime alarmTime;        // 변경할 알람 시간

    private PlanType planType;          // 변경할 계획 유형

    // 반복 계획 관련 필드 (planType이 RECURRING인 경우 사용)
    private RepeatUnit repeatUnit;

    @Min(value = 1, message = "Repeat interval must be at least 1")
    private Integer repeatInterval;

    @Min(value = 1, message = "Repeat day of month must be between 1 and 31")
    @Max(value = 31, message = "Repeat day of month must be between 1 and 31")
    private Integer repeatDayOfMonth;

    private Set<DayOfWeek> repeatWeekdays;

    private Set<LocalDate> exceptionDates;

    // --- 기본 검증 ---

    /** 날짜 순서 검증: startDate ≤ endDate */
    @AssertTrue(message = "Start date must be on or before end date")
    public boolean isDateOrderValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }

    /** 시간 순서 검증: startTime ≤ endTime */
    @AssertTrue(message = "Start time must be on or before end time")
    public boolean isTimeOrderValid() {
        if (startTime == null || endTime == null) {
            return true;
        }
        return !startTime.isAfter(endTime);
    }

    /** SINGLE 타입에는 반복 설정이 없어야 함 */
    @AssertTrue(message = "Single plan should not contain recurrence settings")
    public boolean isSingleWithoutRecurrence() {
        if (planType == PlanType.SINGLE) {
            return repeatUnit == null
                    && (repeatInterval == null || repeatInterval == 0)
                    && repeatDayOfMonth == null
                    && (repeatWeekdays == null || repeatWeekdays.isEmpty());
        }
        return true;
    }

    /** RECURRING 타입에는 최소한 repeatUnit·repeatInterval 이 필요 */
    @AssertTrue(message = "Recurring plan must specify repeat unit and interval")
    public boolean isRecurringBasicValid() {
        if (planType == PlanType.RECURRING) {
            return repeatUnit != null
                    && repeatInterval != null
                    && repeatInterval > 0;
        }
        return true;
    }

    /** WEEKLY 반복에는 repeatWeekdays 가 비어있으면 안됨 */
    @AssertTrue(message = "Weekly recurrence must specify at least one weekday")
    public boolean isWeeklyValid() {
        if (planType == PlanType.RECURRING
                && repeatUnit == RepeatUnit.WEEKLY) {
            return repeatWeekdays != null && !repeatWeekdays.isEmpty();
        }
        return true;
    }

    /** MONTHLY 반복에는 repeatDayOfMonth 가 필요 */
    @AssertTrue(message = "Monthly recurrence must specify day of month")
    public boolean isMonthlyValid() {
        if (planType == PlanType.RECURRING
                && repeatUnit == RepeatUnit.MONTHLY) {
            return repeatDayOfMonth != null;
        }
        return true;
    }

}