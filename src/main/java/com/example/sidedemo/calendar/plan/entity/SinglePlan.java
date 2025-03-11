package com.example.sidedemo.calendar.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("SINGLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SinglePlan extends Plan {
    // 싱글 계획에만 추가할 속성이 있으면 여기에 정의
}