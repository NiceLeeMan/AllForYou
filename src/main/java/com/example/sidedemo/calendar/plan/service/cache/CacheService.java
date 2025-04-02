package com.example.sidedemo.calendar.plan.service.cache;




import com.example.sidedemo.calendar.plan.entity.Plan;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class CacheService {


    //캐시 키를 생성해주는 함수
    public String createMonthCacheKey(LocalDate localDate, Long userId) {
        String key = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return "month:" + key + ":user:" + userId;

    }

    /**
     * 캐시에서 특정 키(연/월, userId)에 해당하는 Plan 리스트를 가져옴.
     * 존재하지 않거나 만료됐으면 null 또는 빈 리스트를 반환.
     */
    public List<Plan> getPlansFromCache(String cacheKey){

    }

    /**
     * Plan을 캐시에 추가하거나 갱신하는 메서드
     * @param plan 캐시에 넣을 Plan 엔티티
     */
    public void saveOrUpdatePlanInCache(Plan plan) {
        // 1) 캐시 키 생성
        // LocalDate는 보통 plan.getStartDate()나 기타 로직을 통해 결정
        // 예: LocalDate keyDate = plan.getStartDate();
        // String cacheKey = createMonthCacheKey(keyDate, plan.getUser().getId());
        // 2) 캐시에서 plan 리스트를 가져와 추가/수정
        // 3) TTL, 만료 시간 설정
    }

    /**
     * Plan을 캐시에서 제거하는 메서드
     *
     * @param planId 삭제할 Plan ID
     * @param localDate  삭제할 plan의 LocalDate (연/월로 캐시 키 추출)
     * @param userId     사용자 식별자
     */
    public void removePlanFromCache(Long planId, LocalDate localDate, Long userId) {
        // 1) 캐시 키 생성
        // 2) 해당 리스트에서 planId에 해당하는 요소 제거
    }

    /**
     * write-behind 전략에 따른 캐시 데이터 → DB 반영
     *
     * 1) 캐시에 저장되어 있는 변경분(Plan)을 특정 주기(스케줄러)나 조건에 따라 DB에 일괄 저장
     * 2) 동시성 문제, 충돌 관리(버전, 타임스탬프 등)는 정책에 맞게 처리
     * 3) 저장 완료 후 캐시에서 해당 Plan을 제거하거나, 상태를 업데이트해서 중복 반영을 방지
     */
    public void syncCacheToDB() {
        // 캐시의 변경분을 조회
        // DB에 batch save
        // 트랜잭션 범위 설정
    }


    /**
     * cache-aside 패턴: 캐시 미스 발생 시 DB에서 읽어와 캐시에 저장
     *
     * @param localDate 특정 일자
     * @param userId    사용자 식별자
     * @return Plan 리스트
     */
    public List<Plan> readFromDBAndCache(LocalDate localDate, Long userId) {
        // DB 조회
        // 캐시에 put
        // return
        return Collections.emptyList();
    }









}
