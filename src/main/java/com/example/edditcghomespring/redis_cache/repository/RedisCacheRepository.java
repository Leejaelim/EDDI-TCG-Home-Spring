package com.example.edditcghomespring.redis_cache.repository;

import java.time.Duration;

public interface RedisCacheRepository {

    /**
     * key-value 저장
     */
    void setKeyAndValue(String key, String value);

    /**
     * key-value 저장 + TTL 적용
     */
    void setKeyAndValue(String key, String value, Duration ttl);

    /**
     * key 기반 value 조회 (타입 안전 Generic)
     */
    <T> T getValueByKey(String key, Class<T> clazz);

    /**
     * key 존재 여부 확인
     */
    boolean exists(String key);

    /**
     * key 삭제
     */
    void deleteByKey(String key);
}