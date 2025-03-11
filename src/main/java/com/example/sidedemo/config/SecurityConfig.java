package com.example.sidedemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 운영 환경에서는 CSRF 보호 및 HTTPS(예: HSTS) 설정 필요 – 여기서는 단순화를 위해 CSRF를 비활성화합니다.
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/plans/**").permitAll()   // 테스트용으로 인증 없이 접근/
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    /**
     * BCryptPasswordEncoder를 스프링 빈으로 등록.
     *
     * - 이를 통해 서비스 계층 등에서 @Autowired/@RequiredArgsConstructor로 주입받아 사용 가능.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}