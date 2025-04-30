package com.example.sidedemo.calendar.plan.service.impl;

import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.calendar.cache.service.CacheService;
import com.example.sidedemo.calendar.plan.dto.create.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.create.CreateResponse;
import com.example.sidedemo.calendar.plan.dto.delete.DeleteRequest;
import com.example.sidedemo.calendar.plan.dto.delete.DeleteResponse;
import com.example.sidedemo.calendar.plan.dto.update.UpdateRequest;
import com.example.sidedemo.calendar.plan.dto.update.UpdateResponse;
import com.example.sidedemo.calendar.plan.entity.Plan;
import com.example.sidedemo.calendar.plan.repository.PlanRepository;
import com.example.sidedemo.User.repository.UserRepository;
import com.example.sidedemo.calendar.plan.service.mapper.PlanMapper;
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
    private final CacheService cacheService;
    private final PlanMapper planMapper;
    private final List<CreateRequest> cacheList = new LinkedList<>();

    /**
     * 계획 생성 (DB에 바로 Insert)
     *
     * @param request CreateRequest DTO
     * @param userId  현재 로그인 사용자 식별자
     * @return CreateResponse DTO
     */

    @Transactional
    public CreateResponse createPlan(CreateRequest request, Long userId) {

        // 1) User 엔티티 조회 : plan을 추가한 사용자가 존해하는지 우선 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id = " + userId));

        // 2) DTO → Plan 엔티티 변환 & DB 저장
        Plan plan = planMapper.dtoToEntity(request, user);
        Plan savedPlan = planRepository.save(plan);

        // 3) Entity → CreateResponse 변환 후 반환
        CreateResponse response = planMapper.entityToCreateResponse(savedPlan);





    }


    /**
     * 월 단위로 Plan 목록을 조회
     *
     * @param localDate 해당 월을 나타내는 임의의 날짜 (예: 2025-04-10)
     * @param userId    사용자 식별자
     * @return Plan 목록
     */
    public List<Plan> readPlan(LocalDate localDate, Long userId) {
        // 1) 캐시 키 생성
        String cacheKey = cacheService.createMonthCacheKey(localDate, userId);

        // 2) 캐시에서 plan 리스트를 가져온다 (직접 CacheService에 메서드를 만들어도 좋음)
        List<Plan> cachedPlans = cacheService.getPlansFromCache(cacheKey);

        // 3) 캐시 미스 시 DB에서 읽어와 캐시에 저장 (cache-aside)
        if (cachedPlans == null || cachedPlans.isEmpty()) {
            cachedPlans = cacheService.readFromDBAndCache(localDate, userId);
        }

        return cachedPlans;
    }

    /**
     * 계획 삭제 (DB에서 바로 Delete)
     *
     * @param request DeleteRequest DTO (planId 포함)
     * @param userId  현재 로그인 사용자 식별자
     * @return DeleteResponse DTO
     */
    @Transactional
    public DeleteResponse deletePlan(DeleteRequest request, Long userId) {
        // 1) DB에서 Plan 조회
        Plan plan = planRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id = " + request.getId()));

        // (옵션) 권한 체크: plan.getUser().getId() == userId ? 등의 비교

        // 2) DB에서 제거
        planRepository.delete(plan);

        // 3) DeleteResponse 생성해서 반환
        return DeleteResponse.builder()
                .id(request.getId())
                .message("Plan deleted successfully.")
                .build();
    }
    /**
     * 계획 수정 (DB에 바로 Update)
     *
     * @param request UpdateRequest DTO
     * @param userId  현재 로그인 사용자 식별자
     * @return UpdateResponse DTO
     */
    @Transactional
    public UpdateResponse updatePlan(UpdateRequest request, Long userId) {
        // 1) DB에서 Plan 조회
        Plan plan = planRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id = " + request.getId()));

        // (옵션) 권한 체크: plan.getUser().getId() == userId ? 등의 비교

        // 2) UpdateRequest 내용을 Plan 엔티티에 반영
        planMapper.applyUpdatesToPlan(plan, request);

        // 3) DB에 저장 (save 호출 시 변경 내용 반영)
        Plan updatedPlan = planRepository.save(plan);

        // 4) UpdateResponse로 변환 후 반환
        return planMapper.entityToUpdateResponse(updatedPlan);
    }



}