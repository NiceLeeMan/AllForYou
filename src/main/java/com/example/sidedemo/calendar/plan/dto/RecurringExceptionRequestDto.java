package com.example.sidedemo.calendar.plan.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringExceptionRequestDto {

    @NotNull(message = "반복 일정 ID는 필수입니다.")
    private Long recurringPlanId;

    @NotNull(message = "예외 발생 날짜는 필수입니다.")
    private LocalDate exceptionDate;

    // isCanceled는 primitive이므로 기본값(false)로 처리
    private boolean isCanceled;

    // newDate는 변경된 날짜가 있을 경우 사용 (선택적)
    private LocalDate newDate;
}
