package com.example.sidedemo.calendar.plan.service.mapper;


import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.calendar.plan.dto.write.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.write.Response;
import com.example.sidedemo.calendar.plan.entity.Plan;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Component
public class Mapper {

    /**
     * CreatePlanRequest DTO를 Plan 엔티티로 변환합니다.
     * - 사용자(User)는 별도로 전달받습니다.
     */
    public Plan createDtoToEntity(CreateRequest request, User user) {
        return Plan.builder()
                .planName(StringUtils.hasText(request.getPlanName()) ? request.getPlanName() : "제목없음")
                .planContent(request.getPlanContent() != null ? request.getPlanContent() : "")
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .alarmTime(request.getAlarmTime())
                .planType(request.getPlanType())
                .repeatUnit(request.getRepeatUnit())
                .repeatInterval(request.getRepeatInterval())
                .repeatDayOfMonth(request.getRepeatDayOfMonth())
                .repeatWeek(request.getRepeatWeek())
                .repeatWeekday(request.getRepeatWeekday())
                .exceptionDates(request.getExceptionDates())
                .user(user)
                .build();
    }


    /**
     * Plan 엔티티를
     */
    public Response entityToWriteResponse(Plan plan) {
        return Response.builder()
                .id(plan.getId())
                .userId(plan.getUser().getId())
                .planName(plan.getPlanName())
                .planContent(plan.getPlanContent())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .startTime(plan.getStartTime())
                .endTime(plan.getEndTime())
                .location(plan.getLocation())
                .alarmTime(plan.getAlarmTime())
                .planType(plan.getPlanType())
                .repeatUnit(plan.getRepeatUnit())
                .repeatInterval(plan.getRepeatInterval())
                .repeatDayOfMonth(plan.getRepeatDayOfMonth())
                .repeatWeek(plan.getRepeatWeek())
                .repeatWeekday(plan.getRepeatWeekday())
                .exceptionDates(plan.getExceptionDates())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }



}

