package com.example.edditcghomespring.redis_cache.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.redis.test.autoconfigure.DataRedisTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@Testcontainers
@Import(RedisCacheRepositoryImpl.class)
class RedisCacheRepositoryTest {

    @Container
    @ServiceConnection
    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:8.2.2-alpine"))
                    .withExposedPorts(6379);

    @Autowired
    private RedisCacheRepository redisCacheRepository;

    @Test
    void 문자열_저장하고_조회() {
        redisCacheRepository.setKeyAndValue("str:key", "hello");

        String result = redisCacheRepository.getValueByKey("str:key", String.class);

        assertThat(result).isEqualTo("hello");
    }

    @Test
    void 정수_저장하고_조회() {
        redisCacheRepository.setKeyAndValue("int:key", "123");

        Integer result = redisCacheRepository.getValueByKey("int:key", Integer.class);

        assertThat(result).isEqualTo(123);
    }

    @Test
    void 롱값_저장하고_조회() {
        redisCacheRepository.setKeyAndValue("long:key", "9876543210");

        Long result = redisCacheRepository.getValueByKey("long:key", Long.class);

        assertThat(result).isEqualTo(9876543210L);
    }

    @Test
    void 불리언_저장하고_조회() {
        redisCacheRepository.setKeyAndValue("bool:key", "true");

        Boolean result = redisCacheRepository.getValueByKey("bool:key", Boolean.class);

        assertThat(result).isTrue();
    }

    @Test
    void UUID_저장하고_조회() {
        UUID uuid = UUID.randomUUID();
        redisCacheRepository.setKeyAndValue("uuid:key", uuid.toString());

        UUID result = redisCacheRepository.getValueByKey("uuid:key", UUID.class);

        assertThat(result).isEqualTo(uuid);
    }

    @Test
    void TTL_만료_검증() throws InterruptedException {
        redisCacheRepository.setKeyAndValue("ttl:key", "value", Duration.ofSeconds(1));

        Thread.sleep(1500);

        String result = redisCacheRepository.getValueByKey("ttl:key", String.class);

        assertThat(result).isNull();
    }

    @Test
    void 존재_여부_검증() {
        redisCacheRepository.setKeyAndValue("exist:key", "value");

        assertThat(redisCacheRepository.exists("exist:key")).isTrue();
        assertThat(redisCacheRepository.exists("not:exist")).isFalse();
    }

    @Test
    void 없는_키_삭제시_예외_없음() {
        redisCacheRepository.deleteByKey("no:key");

        assertThat(redisCacheRepository.exists("no:key")).isFalse();
    }
}