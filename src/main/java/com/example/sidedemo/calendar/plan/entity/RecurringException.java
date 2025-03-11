package com.example.sidedemo.calendar.plan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "recurring_exceptions", indexes = {
        @Index(name = "idx_recurring_plan_id", columnList = "recurring_plan_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringException {

    @Id
    @SequenceGenerator(name = "plan_seq", sequenceName = "plan_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurring_plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_exception_recurring_plan"))
    private RecurringPlan recurringPlan;

    @NotNull
    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;

    @Column(name = "is_canceled", nullable = false)
    private boolean isCanceled;

    @Column(name = "new_date")
    private LocalDate newDate;
}
