package com.example.sidedemo.calendar.common.entity;

import com.example.sidedemo.User.common.entity.User;
import com.example.sidedemo.calendar.plan.enums.Enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.*;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "plans", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_start_date", columnList = "start_date")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Plan {

    private static final Logger log = LoggerFactory.getLogger(Plan.class);

    @Id
    @SequenceGenerator(name = "plan_seq", sequenceName = "plan_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_seq")
    private Long id;

    @NotBlank
    @Column(name = "plan_name", nullable = false, length = 50)
    private String planName;

    @Column(name = "plan_content", columnDefinition = "TEXT" , length = 1000)
    private String planContent;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "location")
    private String location;

    @Column(name = "alarm_time")
    private LocalTime alarmTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 계획 유형을 Enum으로 관리: SINGLE 또는 RECURRING
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    // 반복 계획에 대한 단위: WEEKLY, MONTHLY, YEARLY (반복인 경우에만 의미가 있음)
    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_unit", nullable = true)
    private RepeatUnit repeatUnit; // 예: WEEKLY, MONTHLY, YEARLY

    // 단위 반복 관련 필드
    // 예를 들어, N주 간격 , N년간격
    @Column(name = "repeat_interval", nullable = true)
    private Integer repeatInterval;

    // 월 단위 반복: 일 단위 또는 주/요일 단위 반복을 위한 추가 필드
    // 예: 매월 N일 (일 단위) 또는 둘째 주 수요일 (주/요일 단위)
    @Column(name = "repeat_day_of_month", nullable = true)
    private Integer repeatDayOfMonth; // 일 단위 반복인 경우

    // 반복되는 요일 컬렉션 [월요일] , [월요일, 수요일]
    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "plan_repeat_weekdays",
            joinColumns = @JoinColumn(name = "plan_id"))
    @Enumerated(EnumType.STRING) // enum 이름(MON, TUE, WED...)을 DB에 저장
    @Column(name = "repeat_weekday", nullable = true)
    private Set<DayOfWeek> repeatWeekday = new HashSet<>(); // 주/요일 반복인 경우

    // 년 단위 반복은 보통 start_date를 기반으로 판단하거나 별도 컬럼으로 관리할 수 있음

    // 반복 계획에 대한 예외 날짜 (반복 이벤트 중 특정 날짜를 제외)
    @ElementCollection //collection 객체임을 JPA가 알 수 있게함. 엔티티가 아닌 타입에 대해 테이블을 생성하고 1: N관계로 나눈다.
    @CollectionTable(name = "plan_exceptions", joinColumns = @JoinColumn(name = "plan_id")) // 컬렉션을 매핑할 테이블에대한 역할 지정
    @Column(name = "exception_date")
    private Set<LocalDate> exceptionDates = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_plan_user"))
    private User user;

    @Version
    @Column(name = "version")
    private Long version;
}
