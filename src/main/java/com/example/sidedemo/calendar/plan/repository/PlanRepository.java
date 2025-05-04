package com.example.sidedemo.calendar.plan.repository;


import com.example.sidedemo.calendar.plan.entity.Plan;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository  extends JpaRepository<Plan, Long> {

    /**
     * 한 달치 Plan을 조회할 때 기본적으로 쓰이는 메서드.
     * 주어진 기간에 시작일이 속한 플랜만 가져옵니다.
     */
    List<Plan> findMonthlyPlans(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 지정한 월(periodStart~periodEnd)에 걸쳐 있는(시작일 ≤ periodEnd AND 종료일 ≥ periodStart)
     * 모든 Plan을 조회합니다. 월을跨越하는 플랜도 포함됩니다.
     *
     * @param userId      조회할 사용자 ID
     * @param periodStart 해당 월의 첫째 날 (YYYY-MM-01)
     * @param periodEnd   해당 월의 말일   (YYYY-MM-[28|29|30|31])
     */
    List<Plan> findCrossMonthlyPlans(
            Long userId,
            LocalDate periodStart,
            LocalDate periodEnd
    );
}