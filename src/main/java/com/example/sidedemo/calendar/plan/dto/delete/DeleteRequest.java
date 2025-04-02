package com.example.sidedemo.calendar.plan.dto.delete;


import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Builder
public class DeleteRequest {

    @NotNull(message = "Plan ID is required")
    private Long id;
}
