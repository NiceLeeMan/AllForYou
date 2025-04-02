package com.example.sidedemo.User.entity;



import com.example.sidedemo.calendar.plan.entity.Plan;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름 (실제 이름, 예: 홍길동)
    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    // 사용자 아이디 (로그인 아이디, 고유값)
    @NotBlank(message = "User ID is mandatory")
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    // 사용자 비밀번호 (해시값으로 저장)
    @NotBlank(message = "Password is mandatory")
    @Column(name = "password", nullable = false)
    private String password;

    // 사용자 이메일
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 사용자 휴대폰 번호
    @NotBlank(message = "Phone number is mandatory")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // 유저와 1:N 관계: 하나의 User가 여러 Plan을 가질 수 있음.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Plan> plans;
}
