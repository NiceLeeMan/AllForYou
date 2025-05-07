package com.example.sidedemo.calendar.plan.dto.write;


import com.example.sidedemo.calendar.plan.enums.Enums.PlanType;
import com.example.sidedemo.calendar.plan.enums.Enums.RepeatUnit;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;
import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRequest {

    /**
     * 제목을 주지 않으면 서비스(또는 UI) 레이어에서 "제목없음"으로 채워주세요.
     */
    @Size(max = 100, message = "Plan name must be at most 100 characters")
    private String planName;

    @Size(max = 300, message = "Plan content must be at most 300 characters")
    private String planContent;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @Size(max = 200, message = "Location must be at most 200 characters")
    private String location;

    private LocalTime alarmTime;

    @NotNull(message = "Plan type is required")
    private PlanType planType;

    // 반복 계획 (planType == RECURRING일 때만 사용)
    private RepeatUnit repeatUnit;

    @Min(value = 1, message = "Repeat interval must be at least 1")
    private Integer repeatInterval;

    @Min(value = 1, message = "Repeat day of month must be between 1 and 31")
    @Max(value = 31, message = "Repeat day of month must be between 1 and 31")
    private Integer repeatDayOfMonth;

    private Set<DayOfWeek> repeatWeekdays;

    private Set<LocalDate> exceptionDates;

    // --------------------------------------------------
    // 1) 날짜/시간 순서 검증
    // --------------------------------------------------

    @AssertTrue(message = "Start date must be on or before end date")
    public boolean isDateOrderValid() {
        if (startDate == null || endDate == null) return true;
        return !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "Start time must be on or before end time")
    public boolean isTimeOrderValid() {
        if (startTime == null || endTime == null) return true;
        return !startTime.isAfter(endTime);
    }

    // --------------------------------------------------
    // 2) 단일 계획 vs 반복 계획 검증
    // --------------------------------------------------

    @AssertTrue(message = "Single plan should not contain recurrence settings")
    public boolean isSingleWithoutRecurrence() {
        if (planType == PlanType.SINGLE) {
            return repeatUnit == null
                    && (repeatInterval == null || repeatInterval == 0)
                    && repeatDayOfMonth == null
                    && (repeatWeekdays == null || repeatWeekdays.isEmpty())
                    && (exceptionDates == null || exceptionDates.isEmpty());
        }
        return true;
    }

    @AssertTrue(message = "Recurring plan must specify repeat unit and interval")
    public boolean isRecurringBasicValid() {
        if (planType == PlanType.RECURRING) {
            return repeatUnit != null
                    && repeatInterval != null
                    && repeatInterval > 0;
        }
        return true;
    }

    // 주 단위 반복 → 요일 필수
    @AssertTrue(message = "Weekly recurrence must specify at least one weekday")
    public boolean isWeeklyValid() {
        if (planType == PlanType.RECURRING
                && repeatUnit == RepeatUnit.WEEKLY) {
            return repeatWeekdays != null && !repeatWeekdays.isEmpty();
        }
        return true;
    }

    // 월 단위 반복 → 일자 필수
    @AssertTrue(message = "Monthly recurrence must specify day of month")
    public boolean isMonthlyValid() {
        if (planType == PlanType.RECURRING
                && repeatUnit == RepeatUnit.MONTHLY) {
            return repeatDayOfMonth != null;
        }
        return true;
    }

    // 년 단위 반복 → 월/일 필수 (여기서는 repeatDayOfMonth 사용)
    @AssertTrue(message = "Yearly recurrence must specify day of month")
    public boolean isYearlyValid() {
        if (planType == PlanType.RECURRING
                && repeatUnit == RepeatUnit.YEARLY) {
            return repeatDayOfMonth != null;
        }
        return true;
    }

    // --------------------------------------------------
    // 3) 예외 날짜 범위 검증
    // --------------------------------------------------

    @AssertTrue(message = "Exception dates must fall between startDate and endDate")
    public boolean isExceptionDatesInRange() {
        if (exceptionDates == null || exceptionDates.isEmpty()
                || startDate == null || endDate == null) {
            return true;
        }
        return exceptionDates.stream()
                .allMatch(d -> !d.isBefore(startDate) && !d.isAfter(endDate));
    }
}