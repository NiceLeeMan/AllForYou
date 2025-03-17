package com.example.sidedemo.calendar.plan.repository;


import com.example.sidedemo.calendar.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository  extends JpaRepository<Plan, Long> {
    Optional<Plan> findByPlanId(Long planId);

}
