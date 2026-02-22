package com.example.edditcghomespring.redis_cache.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RedisCacheRepositoryImpl implements RedisCacheRepository {

    private final StringRedisTemplate redisTemplate;

    // 타입별 변환 함수 맵
    private static final Map<Class<?>, Function<String, ?>> TYPE_CAST_MAP = Map.of(
            String.class, Function.identity(),
            Integer.class, Integer::valueOf,
            Long.class, Long::valueOf,
            Boolean.class, Boolean::valueOf,
            Double.class, Double::valueOf,
            UUID.class, UUID::fromString
    );

    @Override
    public void setKeyAndValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setKeyAndValue(String key, String value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValueByKey(String key, Class<T> clazz) {
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String value = valueOps.get(key);

        if (value == null) return null;

        Function<String, ?> caster = TYPE_CAST_MAP.get(clazz);
        if (caster == null) {
            throw new IllegalArgumentException("Unsupported class: " + clazz.getName());
        }

        try {
            return (T) caster.apply(value);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Redis value 변환 실패. key=" + key + ", value=" + value + ", class=" + clazz.getName(), e
            );
        }
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }
}