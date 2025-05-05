package com.example.sidedemo.calendar.plan.dto.read;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadSingleRequest {

    @NotNull
    private Long planId;
}