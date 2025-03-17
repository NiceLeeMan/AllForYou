package com.example.sidedemo.calendar.plan.dto.create;


import com.example.sidedemo.enums.plan.Enums;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecurringCreateRequest extends CreateRequest {

    @NotNull(message = "반복 유형은 필수입니다.")
    private Enums.RecurrenceType repeatType;  // 예: DAILY, WEEKLY, MONTHLY, YEARLY

    @NotBlank(message = "반복 상세 규칙은 필수입니다.")
    private String repeatDetail;  // 예: "월,수,금" 또는 "매 2주"

    @NotNull(message = "반복 간격은 필수입니다.")
    private Integer repeatInterval;  // 예: 1 (매주), 2 (2주마다)
}