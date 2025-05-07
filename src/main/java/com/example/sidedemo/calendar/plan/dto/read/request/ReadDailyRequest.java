package com.example.sidedemo.calendar.plan.dto.read.request;


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

    @NotNull(message = "조회할 날짜는 필수입니다")
    private LocalDate date;
}