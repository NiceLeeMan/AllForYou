package com.example.sidedemo.calendar.plan.dto.read;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ReadDailyResponse {

    private String date;
    private List<ReadResponse> dailyPlans;

}
