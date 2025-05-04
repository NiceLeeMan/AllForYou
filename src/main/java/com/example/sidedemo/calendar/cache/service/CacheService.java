package com.example.sidedemo.calendar.cache.service;




import com.example.sidedemo.calendar.cache.dto.PlanCacheEntry;
import com.example.sidedemo.calendar.plan.dto.write.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CacheService {


    // 1) 월별 인덱스용 (PlanSummary 리스트)
    private final RedisTemplate<String, List<PlanCacheEntry>> monthlyRedisTemplate;

    private static final Duration CACHE_TTL = Duration.ofHours(1);

    // --- 캐시 키 생성 ---
    private String buildMonthKey(Long userId, YearMonth ym) {
        return String.format("calendar:user:%d:month:%04d%02d", userId, ym.getYear(), ym.getMonthValue());
    }

    public void insertToMonthlyCache(Response createdDto) {
        Long userId = createdDto.getUserId();

        // 2) [2025-03, 2025-04, 2025-05], ym : YYYY-MM
        for (YearMonth ym : splitMonths(createdDto.getStartDate(), createdDto.getEndDate())) {
            String monthKey = buildMonthKey(userId, ym);
            List<PlanCacheEntry> list = monthlyRedisTemplate.opsForValue().get(monthKey);

            //캐시에 처음 저장할때
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(PlanCacheEntry.monthlyPlansOverview(createdDto, ym));
            monthlyRedisTemplate.opsForValue().set(monthKey, list, CACHE_TTL);
        }
    }

    public void updateMonthlyCache(Response updatedDto, LocalDate oldStart, LocalDate oldEnd) {

        Long userId = updatedDto.getUserId();
        Long planId = updatedDto.getId();

        // 2) monthly → oldSpan에서 제거, newSpan에서 추가
        Set<YearMonth> oldMonth = splitMonths(oldStart, oldEnd); // oldMonth : [2025-03 , 2025-04 , 2025-05]
        Set<YearMonth> updatedMonth = splitMonths(updatedDto.getStartDate(), updatedDto.getEndDate()); //updatedMonth : [2025-03 , 2025-04]


        Set<YearMonth> affectedMonth = new LinkedHashSet<>(); //affectedMonth : []
        affectedMonth.addAll(oldMonth); //affectedMonth : [2025-03 , 2025-04 , 2025-05]
        affectedMonth.addAll(updatedMonth); //affectedMonth : [2025-03 , 2025-04 , 2025-05]


        for (YearMonth month : affectedMonth) { //affectedMonth : [2025-03 , 2025-04 , 2025-05]를 순회한다. month : "YYYY-MM"

            String monthKey = buildMonthKey(userId, month); //userid-2025-03 , userid-2025-04 , userid-2025-05
            List<PlanCacheEntry> monthPlansList = monthlyRedisTemplate.opsForValue().get(monthKey); //3월달의 PlanList, 4월달의 PlanList , 5월달의 PlanList...
            
            if (monthPlansList == null){
                monthPlansList = new ArrayList<>();
            }

            // 이전 버전 제거
            monthPlansList.removeIf(e -> e.getPlanId().equals(planId)); //monthPlanList = []

            // 업데이트된 Period 에속하면 새 버전에 추가
            if (updatedMonth.contains(month)) {
                monthPlansList.add(PlanCacheEntry.monthlyPlansOverview(updatedDto, month)); //[2025-03] , [2025-03,2025-04] 5월은 포함안됨
            }
            // ex) 5월달에는 계획이 없어질경우 캐시 키 삭제
            if (monthPlansList.isEmpty()) {
                monthlyRedisTemplate.delete(monthKey);
            } else { //캐시에 수정된 PlanList를 반영
                monthlyRedisTemplate.opsForValue().set(monthKey, monthPlansList, CACHE_TTL);
            }
        }
    }

    public void deleteFromMonthlyCache(Long userId, Long planId, LocalDate startDate, LocalDate endDate) {

        for (YearMonth month : splitMonths(startDate, endDate)) {
            String monthKey = buildMonthKey(userId, month);
            List<PlanCacheEntry> monthPlanList = monthlyRedisTemplate
                    .opsForValue()
                    .get(monthKey);
            if (monthPlanList == null) {
                continue;
            }
            // 해당 planId 요약 제거
            monthPlanList.removeIf(e -> e.getPlanId().equals(planId));
            if (monthPlanList.isEmpty()) {
                monthlyRedisTemplate.delete(monthKey);
            } else {
                // 남아 있으면 TTL 갱신하며 덮어쓰기
                monthlyRedisTemplate
                        .opsForValue()
                        .set(monthKey, monthPlanList, CACHE_TTL);
            }
        }
    }

    /**
     * 특정 사용자·월의 캐시된 플랜 요약 리스트를 조회합니다.
     */
    public Optional<List<PlanCacheEntry>> readMonthlyPlansCache(Long userId, YearMonth month) {
        String monthKey = buildMonthKey(userId, month);
        List<PlanCacheEntry> summaries = monthlyRedisTemplate
                .opsForValue()
                .get(monthKey);
        return Optional.ofNullable(summaries);
    }

    /**
     * 특정 사용자·날짜의 캐시에서 단일 플랜 요약을 조회합니다.
     * (월별 리스트에서 필터링)
     */
    public Optional<PlanCacheEntry> readPlanCache(Long userId, LocalDate date, Long planId) {
        YearMonth month = YearMonth.from(date);
        return readMonthlyPlansCache(userId, month)
                .flatMap(list -> list.stream()
                        .filter(e -> e.getPlanId().equals(planId))
                        .findFirst());
    }




    //cross된 month들을 제공 [2025-3 , 2025-4 , 2025-5] 형태로 ㅇ
    private Set<YearMonth> splitMonths(LocalDate startMonth, LocalDate endMonth) {
        //LinkedHashSet이므로 중복된 데이터를 허용하지 않음.
        Set<YearMonth> months = new LinkedHashSet<>();
        YearMonth start = YearMonth.from(startMonth); //start 날짜로부터 첫 번째 YearMonth 객체를 생성합니다. 예를 들어 start가 2025-03-15면, current는 2025-03이 됩니다.
        YearMonth end = YearMonth.from(endMonth);//start 날짜로부터 첫 번째 YearMonth 객체를 생성합니다. 예를 들어 end가 2025-04-2면, end는 2025-04이 됩니다.

        while (!start.isAfter(end)) {
            months.add(start);
            start = start.plusMonths(1);
        }

        return months;
    }


}




