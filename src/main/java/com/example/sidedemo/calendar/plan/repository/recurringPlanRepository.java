package com.example.sidedemo.calendar.plan.repository;


import com.example.sidedemo.calendar.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface recurringPlanRepository extends JpaRepository<Plan, Long> {

}