package com.example.sidedemo.calendar.plan.dto.read.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadMonthlyRequest {

    @NotNull(message = "조회할 연도는 필수입니다")
    @Min(value = 1970, message = "조회 연도는 1970년 이후여야 합니다")
    private Integer year;

    @NotNull(message = "조회할 월은 필수입니다")
    @Min(value = 1, message = "월은 1~12 사이여야 합니다")
    @Max(value = 12, message = "월은 1~12 사이여야 합니다")
    private Integer month;

}
