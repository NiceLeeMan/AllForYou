package com.example.sidedemo.config;

import com.example.sidedemo.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // HTTPS 강제화
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()
                )
                // CSRF 보호: 쿠키 기반 토큰 저장소 사용
                .csrf(csrf -> csrf.disable())
                // 경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/plans","/api/plans/**").permitAll()   // 테스트용으로 인증 없이 접근/
                        .anyRequest().authenticated()
                )
                // 보안 헤더 설정
                .headers(headers -> headers
                        // HSTS: 서브도메인 포함, 1년
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31_536_000)
                        )
                        // CSP: 기본 출처만 허용
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; object-src 'none';")
                        )

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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