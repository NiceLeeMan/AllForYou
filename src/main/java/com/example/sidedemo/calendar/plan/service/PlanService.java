package com.example.sidedemo.calendar.plan.service;

import com.example.sidedemo.calendar.plan.dto.create.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.create.SingleCreateRequest;
import com.example.sidedemo.calendar.plan.entity.Plan;
import com.example.sidedemo.calendar.plan.mapper.planMapper;
import com.example.sidedemo.calendar.plan.entity.RecurringPlan;
import com.example.sidedemo.calendar.plan.entity.SinglePlan;
import com.example.sidedemo.calendar.plan.repository.PlanRepository;
import com.example.sidedemo.domain.User;
import com.example.sidedemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    private final planMapper planMapper;
    private final UserRepository userRepository;


    /* planType == SINGLE
    * 1. SinglePlan - 추가, 수정, 삭제 ,읽기
    * 1-1. createSinglePlan : singlePlanRepository 저장한다.
    * 1-2. updateSinglePlan : 수정된 속성값을 반영해서 singlePlanRepository 저장한다.
    * 1-3. deleteSinglePlan : singlePlanRepository 에서 삭제함
    * 1-4. getSinglePlan :
    * */

    /**
     * 현재 로그인한 사용자의 정보(user)를 별도의 외래키로 설정하여 Plan을 생성합니다.
     *
     * @param createRequest 클라이언트로부터 전달받은 PlanRequestDto
     * @param user       현재 로그인한 사용자 정보(User 객체)
     */
    @Transactional
    public void createSinglePlan(CreateRequest createRequest, User user) {
        //RepeatType에 관한건 Mapper함수가 알아서 변환해줌
        Plan planEntity = planMapper.toPlanEntity(createRequest);
        planEntity.setUser(user);
        planRepository.save(planEntity );
    }

    public SinglePlan updateSinglePlan(SingleCreateRequest RequestDto) {
       ));

    }

    public SinglePlan deleteSinglePlan(SingleCreateRequest RequestDto) {

    }

    public SinglePlan getSinglePlan(SingleCreateRequest RequestDto) {
        
    }



    /* planType == RECURRING
    * 2. recurringPlan - 추가 , 수정, 삭제, 읽기
    * 2-1. createRecurringPlan :
    * 2-2. updateRecurringPlan :
    * 2-3. deleteRecurringPlan :
    * 2-4. getRecurringPlan :
    *
    * */

    public RecurringPlan createRecurringPlan(RecurringPlan recurringPlan) {
        return planRepository.save(recurringPlan);
    }

    public RecurringPlan updateRecurringPlan(RecurringPlan recurringPlan) {
        return planRepository.save(recurringPlan);
    }

    public RecurringPlan deleteRecurringPlan(RecurringPlan recurringPlan) {
        planRepository.delete(recurringPlan);
    }

    public  RecurringPlan getRecurringPlan(RecurringPlan recurringPlan) {

    }








}