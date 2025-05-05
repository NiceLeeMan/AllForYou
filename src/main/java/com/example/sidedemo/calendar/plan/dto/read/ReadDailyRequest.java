package com.example.sidedemo.calendar.plan.dto.read;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * 하루치(YYYY-MM-DD) Plan 목록을 조회하기 위한 요청 DTO.
 */

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadDailyRequest {

    @NotNull
    private LocalDate date;
}