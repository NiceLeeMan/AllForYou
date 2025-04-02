package com.example.sidedemo.calendar.plan.service.mapper;


import com.example.sidedemo.User.entity.User;
import com.example.sidedemo.calendar.plan.dto.create.CreateRequest;
import com.example.sidedemo.calendar.plan.dto.create.CreateResponse;
import com.example.sidedemo.calendar.plan.dto.delete.DeleteResponse;
import com.example.sidedemo.calendar.plan.dto.update.UpdateRequest;
import com.example.sidedemo.calendar.plan.dto.update.UpdateResponse;
import com.example.sidedemo.calendar.plan.entity.Plan;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PlanMapper {

    /**
     * CreatePlanRequest DTO를 Plan 엔티티로 변환합니다.
     * - 사용자(User)는 별도로 전달받습니다.
     */
    public Plan dtoToEntity(CreateRequest request, User user) {
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
     * Plan 엔티티를 CreatePlanResponse DTO로 변환합니다.
     */
    public CreateResponse entityToCreateResponse(Plan plan) {
        return CreateResponse.builder()
                .id(plan.getId())
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

    /**
     * Plan 엔티티를 ReadPlanResponse DTO로 변환합니다.
     * - 읽기 전용 응답 DTO로, UI에 표시할 데이터를 구성합니다.
     */


    /**
     * PatchPlanRequest DTO에 담긴 변경 사항을 기존 Plan 엔티티에 적용합니다.
     * - 각 필드가 null이 아닌 경우에만 업데이트합니다.
     */
    public void applyUpdatesToPlan(Plan plan,UpdateRequest request) {
        if (request.getPlanName() != null) { plan.setPlanName(request.getPlanName()); }
        if (request.getPlanContent() != null) { plan.setPlanContent(request.getPlanContent()); }
        if (request.getStartDate() != null) { plan.setStartDate(request.getStartDate()); }
        if (request.getEndDate() != null) { plan.setEndDate(request.getEndDate()); }
        if (request.getStartTime() != null) { plan.setStartTime(request.getStartTime()); }
        if (request.getEndTime() != null) { plan.setEndTime(request.getEndTime()); }
        if (request.getLocation() != null) { plan.setLocation(request.getLocation()); }
        if (request.getAlarmTime() != null) { plan.setAlarmTime(request.getAlarmTime()); }
        if (request.getPlanType() != null) { plan.setPlanType(request.getPlanType()); }
        if (request.getRepeatUnit() != null) { plan.setRepeatUnit(request.getRepeatUnit()); }
        if (request.getRepeatInterval() != null) { plan.setRepeatInterval(request.getRepeatInterval()); }
        if (request.getRepeatDayOfMonth() != null) { plan.setRepeatDayOfMonth(request.getRepeatDayOfMonth()); }
        if (request.getRepeatWeek() != null) { plan.setRepeatWeek(request.getRepeatWeek()); }
        if (request.getRepeatWeekday() != null) { plan.setRepeatWeekday(request.getRepeatWeekday()); }
        if (request.getExceptionDates() != null) { plan.setExceptionDates(request.getExceptionDates()); }
    }
    public UpdateResponse entityToUpdateResponse(Plan plan) {
        return UpdateResponse.builder()
                .id(plan.getId())
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
    /**
     * 삭제 후 결과를 반환할 DeletePlanResponse DTO로 변환합니다.
     */
    public DeleteResponse toDeletePlanResponse(Plan plan) {
        return DeleteResponse.builder()
                .id(plan.getId())
                .message("Plan deleted successfully")
                .build();
    }
}

