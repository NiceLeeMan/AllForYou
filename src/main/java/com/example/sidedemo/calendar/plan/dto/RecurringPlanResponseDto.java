package com.example.sidedemo.calendar.plan.dto;

import com.example.sidedemo.enums.plan.Enums;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecurringPlanResponseDto extends PlanResponseDto {
    private Enums.RecurrenceType repeatType;
    private String repeatDetail;
    private Integer repeatInterval;
}