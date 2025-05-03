package com.example.sidedemo.calendar.cache.service;




import com.example.sidedemo.calendar.cache.dto.PlanCacheEntry;
import com.example.sidedemo.calendar.plan.dto.write.PlanWriteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    public void createPlanCache(PlanWriteResponse dto) {
        Long userId = dto.getUserId();

        // 2) monthlyCache
        for (YearMonth ym : splitMonths(dto.getStartDate(), dto.getEndDate())) {
            String monthKey = buildMonthKey(userId, ym);
            List<PlanCacheEntry> list = monthlyRedisTemplate.opsForValue().get(monthKey);
            if (list == null) list = new ArrayList<>();
            list.add(PlanCacheEntry.fromSummary(dto, ym));
            monthlyRedisTemplate.opsForValue().set(monthKey, list, CACHE_TTL);
        }
    }

    public void updatePlanCache(PlanWriteResponse dto, LocalDate oldStart, LocalDate oldEnd) {

        Long userId = dto.getUserId();
        Long planId = dto.getId();


        // 2) monthly → oldSpan에서 제거, newSpan에서 추가
        Set<YearMonth> oldMonth = splitMonths(oldStart, oldEnd); //[2025-03 , 2025-04 , 2025-05]
        Set<YearMonth> updatedMonth = splitMonths(dto.getStartDate(), dto.getEndDate()); //[2025-03 , 2025-04]


        Set<YearMonth> affctedMonths = new LinkedHashSet<>();
        affctedMonths.addAll(oldMonth);
        affctedMonths.addAll(updatedMonth);

        for (YearMonth month : affctedMonths) {
            
            String monthKey = buildMonthKey(userId, month);
            
            List<PlanCacheEntry> summaries = monthlyRedisTemplate.opsForValue().get(monthKey);
            
            if (summaries == null){
                summaries = new ArrayList<>();
            }

            // 이전 버전(summary)제거

            summaries.removeIf(e -> e.getPlanId().equals(planId));

            // 업데이트된 span에 속하면 새 버전에 추가
            if (updatedMonth.contains(month)) {
                summaries.add(PlanCacheEntry.fromSummary(dto, month));
            }

            // 비어 있으면 삭제, 아니면 덮어쓰기
            if (summaries.isEmpty()) {
                monthlyRedisTemplate.delete(monthKey);
            } else {
                monthlyRedisTemplate.opsForValue().set(monthKey, summaries, CACHE_TTL);
            }
        }
    }



    //cross된 month들을 제공 [2025-3 , 2025-4 , 2025-5] 형태로 ㅇ
    private Set<YearMonth> splitMonths(LocalDate start, LocalDate end) {
        //LinkedHashSet이므로 중복된 데이터를 허용하지 않음.
        Set<YearMonth> months = new LinkedHashSet<>();
        YearMonth current = YearMonth.from(start); //start 날짜로부터 첫 번째 YearMonth 객체를 생성합니다. 예를 들어 start가 2025-03-15면, current는 2025-03이 됩니다.
        YearMonth last = YearMonth.from(end);//start 날짜로부터 첫 번째 YearMonth 객체를 생성합니다. 예를 들어 end가 2025-04-2면, end는 2025-04이 됩니다.

        while (!current.isAfter(last)) {
            months.add(current);
            current = current.plusMonths(1);
        }

        return months;
    }







}
