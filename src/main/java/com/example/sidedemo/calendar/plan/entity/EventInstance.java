package com.example.sidedemo.calendar.plan.entity;

import com.example.sidedemo.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
@Table(name = "event_instances", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_event_date", columnList = "event_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventInstance {

    @Id
    @SequenceGenerator(name = "event_instance_seq", sequenceName = "event_instance_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_instance_seq")
    private Long id;


    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_event_instance_plan"))
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_event_instance_user"))
    private User user;
}
