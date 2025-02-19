package com.example.sidedemo.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계획 이름 (동일 이름 가능)
    @NotBlank(message = "Plan name is mandatory")
    @Column(name = "plan_name", nullable = false)
    private String planName;

    // 상세 내용 (부가설명)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // 계획 날짜
    @NotNull(message = "Plan date is mandatory")
    @Column(name = "plan_date", nullable = false)
    private LocalDate planDate;

    // 시작 시간
    @NotNull(message = "Start time is mandatory")
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // 종료 시간
    @NotNull(message = "End time is mandatory")
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // 야외 약속 여부
    @Column(name = "is_outdoor", nullable = false)
    private boolean outdoor;

    // 야외 약속인 경우의 위치 (야외 약속이 아니면 null 허용)
    @Column(name = "location")
    private String location;

    // User와의 다대일 관계 (각 Plan은 반드시 한 User에 속함)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
