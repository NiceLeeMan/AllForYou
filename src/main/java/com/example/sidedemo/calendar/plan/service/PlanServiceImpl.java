package com.example.sidedemo.calendar.plan.service;

import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.calendar.cache.dto.PlanCacheEntry;
import com.example.sidedemo.calendar.cache.service.CacheService;
import com.example.sidedemo.calendar.plan.dto.write.*;
import com.example.sidedemo.calendar.plan.entity.Plan;
import com.example.sidedemo.calendar.plan.repository.PlanRepository;
import com.example.sidedemo.User.repository.UserRepository;
import com.example.sidedemo.calendar.plan.service.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
    public Response createPlan(CreateRequest request, Long userId) {

        // 1) User 엔티티 조회 : plan을 추가한 사용자가 존해하는지 우선 확인
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id = " + userId));

        // 2) DTO → Plan 엔티티 변환 & DB 저장
        Plan plan = planMapper.createDtoToEntity(request,currentUser);
        Plan savedPlan = planRepository.save(plan);

        //3)Entity->DTO 변환후 캐시 저장
        Response response = planMapper.entityToWriteResponse(savedPlan);
        cacheService.insertToMonthlyCache(response);

        return response;

    }

    @Transactional
    public Response updatePlan(UpdateRequest newPlan, Long userId) {

        //수정전 Plan을 가져옴
        Plan oldPlan = planRepository.findById(newPlan.getId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id = " + newPlan.getId()));


        // 2) 소유권 검증(Optional)
        if (!oldPlan.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to update this plan");
        }

        //oldPlan -> newPlan 후 DB에 저장
        Plan updatedPlan = updateToPlan(oldPlan, newPlan);
        planRepository.save(updatedPlan);

        //캐시에 저장하기 위해 다시 DTO형태로 변환
        Response response = planMapper.entityToWriteResponse(updatedPlan);

        cacheService.updateMonthlyCache
                (response,
                oldPlan.getStartDate(),
                oldPlan.getEndDate()
                );

        return Response.builder()
                .id(updatedPlan.getId())
                .message("수정되었습니다.")
                .build();

    }

    /**
     * Plan 삭제 (DB + 캐시)
     */
    @Transactional
    public Response deletePlan(DeleteRequest deleteRequest, Long userId) {

        // 1) DB에서 기존 Plan 조회 및 소유권 검증
        Plan existing = planRepository.findById(deleteRequest.getId())
                .orElseThrow(() -> new RuntimeException("Plan not found with id = " + deleteRequest.getId()));
        if (!existing.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this plan");
        }

        // 2) DB 삭제
        planRepository.delete(existing);

        // 3) 캐시 삭제 (old span)
        cacheService.deleteFromMonthlyCache(
                userId,
                existing.getId(),
                existing.getStartDate(),
                existing.getEndDate()
        );
        // 4) 응답
        return Response.builder()
                .id(existing.getId())
                .message("Deleted")
                .build();

    }

    @Transactional
    public Response deleteOccurrence(Long planId, LocalDate date, Long userId) {
        // 1) Plan 조회 및 소유권 검증
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));
        if (!plan.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this occurrence");
        }

        // 2) 예외 날짜 추가 및 DB 저장
        Set<LocalDate> exceptions = plan.getExceptionDates();
        exceptions.add(date);
        plan.setExceptionDates(exceptions);
        planRepository.save(plan);

        // 3) 캐시에서 해당 회차(날짜)만 제거
        cacheService.deleteFromMonthlyCache(
                userId,
                planId,
                date,    // span start
                date     // span end
        );

        // 4) 응답
        return Response.builder()
                .id(planId)
                .message("Deleted occurrence on " + date)
                .build();
    }

        /**
         * 한 달치 Plan 목록 조회 (캐시 우선 → DB fallback)
        */
        @Transactional(readOnly = true)
        public List<Response> readMonthlyPlans(Long userId, YearMonth month) {
            // 1) 캐시에서 요약 리스트 조회
            return cacheService.readMonthlyPlansCache(userId, month)
                    .map(entries ->
                            entries.stream()
                                    .map(entry -> {
                                        // summary에는 내용이 없으므로 DB에서 전체 Entity 로딩
                                        Plan plan = planRepository.findById(entry.getPlanId())
                                                .orElseThrow(() -> new RuntimeException("Plan not found: " + entry.getPlanId()));
                                        return planMapper.entityToWriteResponse(plan);
                                    })
                                    .collect(Collectors.toList())
                    )
                    // 2) 캐시 미스 시 DB에서 cross‐month 포함 조회
                    .orElseGet(() -> {
                        LocalDate start = month.atDay(1);
                        LocalDate end   = month.atEndOfMonth();
                        List<Plan> plans = planRepository
                                .findCrossMonthlyPlans(userId, start, end);
                        List<Response> responses = plans.stream()
                                .map(planMapper::entityToWriteResponse)
                                .collect(Collectors.toList());
                        // 3) 캐시 채우기
                        responses.forEach(cacheService::insertToMonthlyCache);
                        return responses;
                    });
        }



    @Transactional(readOnly = true)
    public List<Response> readDailyPlans(Long userId, LocalDate date) {
        YearMonth month = YearMonth.from(date);

        // 1) 월별 캐시 조회 (미리 계산된 segStart/segEnd 기준으로 필터링)
        List<PlanCacheEntry> DailyPlans = cacheService
                .readMonthlyPlansCache(userId, month)
                .orElseGet(() -> {
                    // 캐시 미스 시 월별 전체 로드
                    LocalDate start = month.atDay(1);
                    LocalDate end   = month.atEndOfMonth();
                    List<Plan> plans = planRepository
                            .findCrossMonthlyPlans(userId, start, end);
                    List<Response> loaded = plans.stream()
                            .map(planMapper::entityToWriteResponse)
                            .collect(Collectors.toList());
                    loaded.forEach(cacheService::insertToMonthlyCache);
                    return cacheService.readMonthlyPlansCache(userId, month).get();
                });

        // 2) 하루치(Date) 필터링
        return DailyPlans.stream()
                .filter(e -> !date.isBefore(e.getSegStart())
                        && !date.isAfter(e.getSegEnd()))
                .map(e -> {
                    // 상세 시간(startTime~endTime)까지 DTO에 포함해야 하므로
                    Plan plan = planRepository.findById(e.getPlanId())
                            .orElseThrow();
                    return planMapper.entityToWriteResponse(plan);
                })
                .collect(Collectors.toList());
    }


    /**
     * 단일 Plan 상세 조회 (캐시 요약 확인 → DB 조회 → 캐시 보강)
     */
    @Transactional(readOnly = true)
    public Response readPlan(Long userId, Long planId) {
        // 1) DB에서 조회 및 소유권 검증
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));
        if (!plan.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        // 2) 캐시에 요약이 존재하면(=이미 월별 인덱싱 됐으면) 바로 응답 DTO 생성
        LocalDate date = plan.getStartDate();
        if (cacheService.readPlanCache(userId, date, planId).isPresent()) {
            return planMapper.entityToWriteResponse(plan);
        }

        // 3) 캐시 미스 시, 월별 캐시에 요약 추가
        Response response = planMapper.entityToWriteResponse(plan);
        cacheService.insertToMonthlyCache(response);
        return response;
    }




    public Plan updateToPlan(Plan plan, UpdateRequest req) {
        plan.setPlanName(StringUtils.hasText(req.getPlanName())
                ? req.getPlanName() : "제목없음");
        plan.setPlanContent(req.getPlanContent() != null
                ? req.getPlanContent() : "");
        plan.setStartDate(req.getStartDate());
        plan.setEndDate(req.getEndDate());
        plan.setStartTime(req.getStartTime());
        plan.setEndTime(req.getEndTime());
        plan.setLocation(req.getLocation());
        plan.setAlarmTime(req.getAlarmTime());
        plan.setPlanType(req.getPlanType());
        plan.setRepeatUnit(req.getRepeatUnit());
        plan.setRepeatInterval(req.getRepeatInterval());
        plan.setRepeatDayOfMonth(req.getRepeatDayOfMonth());
        plan.setRepeatWeekday(req.getRepeatWeekdays());
        plan.setExceptionDates(req.getExceptionDates());
        plan.setUpdatedAt(LocalDateTime.now());

        // userId, createdAt 은 그대로 유지

        return plan;
    }


}