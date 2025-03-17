package com.example.sidedemo.calendar.plan.dto.create;

import com.example.sidedemo.enums.plan.Enums;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecurringCreateResponse extends CreateResponse {
    private Enums.RecurrenceType repeatType;
    private String repeatDetail;
    private Integer repeatInterval;
}