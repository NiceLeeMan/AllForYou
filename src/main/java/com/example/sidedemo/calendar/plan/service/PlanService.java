package com.example.sidedemo.calendar.plan.service;

import com.example.sidedemo.calendar.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;



    /* planType == SINGLE
    * 1. SinglePlan - 추가, 수정, 삭제 ,읽기
    * 1-1. createSinglePlan : singlePlanRepository 저장한다.
    * 1-2. updateSinglePlan : 수정된 속성값을 반영해서 singlePlanRepository 저장한다.
    * 1-3. deleteSinglePlan : singlePlanRepository 에서 삭제함
    * 1-4. getSinglePlan :
    * */

    /* planType == RECURRING
    * 2. recurringPlan - 추가 , 수정, 삭제, 읽기
    * 2-1. createRecurringPlan :
    * 2-2. updateRecurringPlan :
    * 2-3. deleteRecurringPlan :
    * 2-4. getRecurringPlan :
    *
    * */







}