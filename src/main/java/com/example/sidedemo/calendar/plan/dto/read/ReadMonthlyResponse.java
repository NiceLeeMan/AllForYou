package com.example.sidedemo.calendar.plan.dto.read;


import lombok.Getter;

import java.util.List;

@Getter
public class ReadMonthlyResponse {

    private String month;

    private List<ReadResponse> plans;
}
