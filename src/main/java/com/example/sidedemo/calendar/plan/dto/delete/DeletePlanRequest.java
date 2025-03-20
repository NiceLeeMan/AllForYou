package com.example.sidedemo.calendar.plan.dto.delete;


import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter

public class DeletePlanRequest {

    @NotNull(message = "Plan ID is required")
    private Long id;
}
