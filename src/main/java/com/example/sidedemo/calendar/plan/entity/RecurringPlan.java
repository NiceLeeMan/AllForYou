package com.example.sidedemo.calendar.plan.entity;




import com.example.sidedemo.enums.plan.Enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@DiscriminatorValue("RECURRING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecurringPlan extends Plan {

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_type", nullable = false)
    private RecurrenceType repeatType;  // 반복 유형

    @Column(name = "repeat_detail")
    private String repeatDetail;  // 상세 반복 규칙

    @Column(name = "repeat_interval")
    private Integer repeatInterval;  // 반복 간격


}

