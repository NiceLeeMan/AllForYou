package com.example.sidedemo.calendar.plan.dto.write;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@Getter
@NoArgsConstructor    // Jackson 역직렬화용 기본 생성자
@AllArgsConstructor
@Builder
public class DeleteRequest {

    @NotNull(message = "Plan ID is required")
    @Positive(message = "Plan ID는 양수여야 합니다")
    private Long id;
}
