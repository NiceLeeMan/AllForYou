package com.example.sidedemo.calendar.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean

    public ObjectMapper redisObjectMapper() {
        ObjectMapper om = new ObjectMapper()
                // LocalDate, LocalDateTime 등 JSR-310 타입 지원
                .registerModule(new JavaTimeModule())
                // ISO-8601 문자열 포맷으로 직렬화 (타임스탬프 비활성)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return om;
    }

    // (기존) 기본 템플릿
    /** ② 기본 RedisTemplate (Object 타입 값) */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory cf,
            ObjectMapper redisObjectMapper) {

        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);

        // Key: String
        tpl.setKeySerializer(new StringRedisSerializer());

        // Value: jacksonObjectMapper 기반 JSON
        GenericJackson2JsonRedisSerializer jacksonSer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);

        tpl.setValueSerializer(jacksonSer);
        tpl.setHashValueSerializer(jacksonSer);
        tpl.afterPropertiesSet();
        return tpl;
    }

    /** ③ 월별 캐시 전용 템플릿 (PlanCacheEntry 리스트) */
    @Bean
    public RedisTemplate<String, List<PlanCacheEntry>> monthlyRedisTemplate(
            RedisConnectionFactory cf) {
        // 1) ObjectMapper에 타입 정보 포함 설정
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // LocalDate, LocalTime 지원
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // 2) GenericJackson2JsonRedisSerializer에 커스텀 매퍼 주입
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(mapper);

        RedisTemplate<String, List<PlanCacheEntry>> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(serializer);
        tpl.afterPropertiesSet();  // **반드시** 호출해야 정상 동작합니다

        return tpl;
    }

    /** ④ 상세 캐시 전용 템플릿 (단일 PlanCacheEntry) */
    @Bean
    public RedisTemplate<String, PlanCacheEntry> detailRedisTemplate(
            RedisConnectionFactory cf,
            ObjectMapper redisObjectMapper) {

        RedisTemplate<String, PlanCacheEntry> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer jacksonSer =
                new GenericJackson2JsonRedisSerializer(redisObjectMapper);

        tpl.setValueSerializer(jacksonSer);
        tpl.setHashValueSerializer(jacksonSer);
        tpl.afterPropertiesSet();
        return tpl;
    }
}