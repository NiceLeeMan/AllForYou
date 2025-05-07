package com.example.sidedemo.calendar.plan.dto.read.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadSingleRequest {

    @NotNull(message = "조회할 계획 ID는 필수입니다")
    @Positive(message = "계획 ID는 양수여야 합니다")
    private Long planId;
}