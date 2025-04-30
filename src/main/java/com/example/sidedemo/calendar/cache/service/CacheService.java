package com.example.sidedemo.calendar.cache.service;




import com.example.sidedemo.calendar.cache.dto.PlanCacheEntry;
import com.example.sidedemo.calendar.cache.dto.UpsertDto;
import com.example.sidedemo.calendar.plan.dto.create.CreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    // 2) 단일 플랜 상세용
    private final RedisTemplate<String, PlanCacheEntry> detailRedisTemplate;

    private static final Duration CACHE_TTL = Duration.ofHours(1);


    // --- 캐시 키 생성 ---
    private String buildMonthKey(Long userId, YearMonth ym) {
        return String.format("calendar:user:%d:month:%04d%02d", userId, ym.getYear(), ym.getMonthValue());
    }

    private String buildDetailKey(Long userId, Long planId) {
        return String.format("calendar:plan:user:%d:plan:%d", userId, planId);
    }

    public void upsertPlanCache(UpsertDto dto) {

        Long planId = dto.getId();
        Long userId = dto.getUserId();


        String detailKey = buildDetailKey(userId, planId);
        PlanCacheEntry detail = PlanCacheEntry.fromDetail(dto);

        detailRedisTemplate.opsForValue().set(detailKey,detail,CACHE_TTL);



        for (YearMonth ym : splitMonths(dto.getStartDate(), dto.getEndDate())) {

            String key = buildMonthKey(userId, ym);
            List<PlanCacheEntry> summaries = monthlyRedisTemplate.opsForValue().get(key);

            if (summaries == null) {
                summaries = new ArrayList<>();
            }
            summaries.add(PlanCacheEntry.fromSummary(dto, ym));
            monthlyRedisTemplate.opsForValue().set(key, summaries, CACHE_TTL);
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
