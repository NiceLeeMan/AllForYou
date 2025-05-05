package com.example.sidedemo.calendar.plan.dto.read;


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

    @NotNull
    @Min(1970)
    private Integer year;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;

}
