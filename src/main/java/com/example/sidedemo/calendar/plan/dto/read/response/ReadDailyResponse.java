package com.example.sidedemo.calendar.plan.dto.read.response;


import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class ReadDailyResponse {

    private String date;
    private List<SingleResponse> dailyPlans;

}
