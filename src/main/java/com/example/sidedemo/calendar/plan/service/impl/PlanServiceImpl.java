package com.example.sidedemo.calendar.plan.service.impl;

import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.calendar.cache.service.CacheService;
import com.example.sidedemo.calendar.plan.dto.write.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.write.PlanWriteRespnse;
import com.example.sidedemo.calendar.plan.dto.write.PlanWriteResponse;
import com.example.sidedemo.calendar.plan.dto.write.UpdateRequest;
import com.example.sidedemo.calendar.plan.entity.Plan;
import com.example.sidedemo.calendar.plan.repository.PlanRepository;
import com.example.sidedemo.User.repository.UserRepository;
import com.example.sidedemo.calendar.plan.service.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PlanServiceImpl {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final Mapper planMapper;
    private final CacheService cacheService;

    /**
     * 계획 생성 (DB에 바로 Insert)
     *
     * @param request CreateRequest DTO
     * @param userId  현재 로그인 사용자 식별자
     * @return CreateResponse DTO
     */

    @Transactional
    public PlanWriteResponse createPlan(CreateRequest request, Long userId) {

        // 1) User 엔티티 조회 : plan을 추가한 사용자가 존해하는지 우선 확인
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id = " + userId));

        // 2) DTO → Plan 엔티티 변환 & DB 저장
        Plan plan = planMapper.createDtoToEntity(request,currentUser);
        Plan savedPlan = planRepository.save(plan);
        PlanWriteResponse response = planMapper.entityToWriteResponse(savedPlan);
        cacheService.createPlanCache(response);

        return response;

    }

    @Transactional
    public PlanWriteResponse updatePlan(UpdateRequest request, Long userId) {

        Plan oldPlan = planRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id = " + request.getId()));

        // 2) 소유권 검증(Optional)
        if (!oldPlan.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this plan");
        }

        LocalDate oldStartDate = oldPlan.getStartDate();
        LocalDate oldEndDate = oldPlan.getEndDate();

        //Dto->Entity & DB 저장
        Plan updateedPlan = planMapper.updateFromDto(oldPlan, request);
        Plan savedPlan = planRepository.save(updateedPlan);


        PlanWriteResponse response = planMapper.entityToWriteResponse(savedPlan);
        cacheService.updatePlanCache(response,oldStartDate,oldEndDate);
        return response;


    }

















}