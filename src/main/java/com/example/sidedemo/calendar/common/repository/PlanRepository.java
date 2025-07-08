package com.example.sidedemo.calendar.common.repository;


import com.example.sidedemo.calendar.common.entity.Plan;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository  extends JpaRepository<Plan, Long> {

    /** 한 달치: 시작일이 해당 월에 속하는 플랜만 */
    List<Plan> findAllByUserIdAndStartDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    /** 월跨越 플랜 포함: startDate ≤ periodEnd AND endDate ≥ periodStart */
    @Query("SELECT p FROM Plan p " +
            " WHERE p.user.id      = :userId" +
            "   AND p.startDate   <= :periodEnd" +
            "   AND p.endDate     >= :periodStart")
    List<Plan> findCrossMonthlyPlans(
            @Param("userId")       Long userId,
            @Param("periodStart")  LocalDate periodStart,
            @Param("periodEnd")    LocalDate periodEnd
    );
}