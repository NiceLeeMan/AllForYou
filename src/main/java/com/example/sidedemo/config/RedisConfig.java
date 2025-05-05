package com.example.sidedemo.config;

import com.example.sidedemo.calendar.cache.dto.PlanCacheEntry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    // (기존) 기본 템플릿
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return tpl;
    }

    // ① 월별 요약 리스트용
    @Bean
    public RedisTemplate<String, List<PlanCacheEntry>> monthlyRedisTemplate(
            RedisConnectionFactory cf) {
        RedisTemplate<String, List<PlanCacheEntry>> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return tpl;
    }

    // (추가) 필요 시 단일 상세용
    @Bean
    public RedisTemplate<String, PlanCacheEntry> detailRedisTemplate(
            RedisConnectionFactory cf) {
        RedisTemplate<String, PlanCacheEntry> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return tpl;
    }
}