package com.example.sidedemo.calendar.plan.mapper;


import com.example.sidedemo.calendar.plan.dto.*;
import com.example.sidedemo.calendar.plan.entity.Plan;
import com.example.sidedemo.calendar.plan.entity.RecurringException;
import com.example.sidedemo.calendar.plan.entity.RecurringPlan;
import com.example.sidedemo.calendar.plan.entity.SinglePlan;
import org.springframework.stereotype.Component;

@Component
public class planMapper {

    // DTO -> Entity (양방향 중 반대쪽)
    public Plan toPlanEntity(PlanRequestDto dto) {

        if ("RECURRING".equalsIgnoreCase(dto.getPlanType())) {

            RecurringPlanRequestDto recurringDto = (RecurringPlanRequestDto) dto;

            return RecurringPlan.builder()
                    .planName(recurringDto.getPlanName())
                    .planContent(recurringDto.getPlanContent())
                    .startDate(recurringDto.getStartDate())
                    .endDate(recurringDto.getEndDate())
                    .startTime(recurringDto.getStartTime())
                    .endTime(recurringDto.getEndTime())
                    .location(recurringDto.getLocation())
                    .alarmTime(recurringDto.getAlarmTime())
                    .repeatType(recurringDto.getRepeatType())
                    .repeatDetail(recurringDto.getRepeatDetail())
                    .repeatInterval(recurringDto.getRepeatInterval())
                    .planType("RECURRING")
                    // user 정보는 서비스 계층에서 별도로 설정합니다.
                    .build();

        } else {
            // SINGLE 타입
            SinglePlanRequestDto singleDto = (SinglePlanRequestDto) dto;

            return SinglePlan.builder()
                    .planName(singleDto.getPlanName())
                    .planContent(singleDto.getPlanContent())
                    .startDate(singleDto.getStartDate())
                    .endDate(singleDto.getEndDate())
                    .startTime(singleDto.getStartTime())
                    .endTime(singleDto.getEndTime())
                    .location(singleDto.getLocation())
                    .alarmTime(singleDto.getAlarmTime())
                    .planType("SINGLE")
                    .build();
        }
    }


    // Entity -> DTO (양방향 중 한쪽)
    public PlanResponseDto toPlanResponseDTO(Plan plan) {

        if ("RECURRING".equalsIgnoreCase(plan.getPlanType()) && plan instanceof RecurringPlan) {
            RecurringPlan recurring = (RecurringPlan) plan;

            return RecurringPlanResponseDto.builder()
                    .id(recurring.getId())
                    .planName(recurring.getPlanName())
                    .planContent(recurring.getPlanContent())
                    .startDate(recurring.getStartDate())
                    .endDate(recurring.getEndDate())
                    .startTime(recurring.getStartTime())
                    .endTime(recurring.getEndTime())
                    .location(recurring.getLocation())
                    .alarmTime(recurring.getAlarmTime())
                    .createdAt(recurring.getCreatedAt())
                    .updatedAt(recurring.getUpdatedAt())
                    .planType(recurring.getPlanType())
                    .repeatDetail(recurring.getRepeatDetail())
                    .repeatInterval(recurring.getRepeatInterval())
                    .repeatType(recurring.getRepeatType())
                    .build();

        } else {
            // SINGLE 타입 (또는 기본 PlanResponseDto로 처리)
            return PlanResponseDto.builder()
                    .id(plan.getId())
                    .planName(plan.getPlanName())
                    .planContent(plan.getPlanContent())
                    .startDate(plan.getStartDate())
                    .endDate(plan.getEndDate())
                    .startTime(plan.getStartTime())
                    .endTime(plan.getEndTime())
                    .location(plan.getLocation())
                    .alarmTime(plan.getAlarmTime())
                    .createdAt(plan.getCreatedAt())
                    .updatedAt(plan.getUpdatedAt())
                    .planType(plan.getPlanType())
                    .build();
        }
    }

    public RecurringExceptionResponseDto toRecurringExceptionResponseDTO(RecurringException exception) {
        return RecurringExceptionResponseDto.builder()
                .id(exception.getId())
                .recurringPlanId(exception.getRecurringPlan().getId())
                .exceptionDate(exception.getExceptionDate())
                .isCanceled(exception.isCanceled())
                .newDate(exception.getNewDate())
                .build();
    }

    public RecurringException toRecurringExceptionEntity(RecurringExceptionRequestDto dto) {
        return RecurringException.builder()
                // recurringPlan은 서비스 계층에서 설정 (DTO에서 ID만 전달)
                .exceptionDate(dto.getExceptionDate())
                .isCanceled(dto.isCanceled())
                .newDate(dto.getNewDate())
                .build();
    }
}


