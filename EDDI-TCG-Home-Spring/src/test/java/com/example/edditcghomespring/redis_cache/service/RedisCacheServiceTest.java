package com.example.edditcghomespring.redis_cache.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Testcontainers
@Import(RedisCacheServiceImpl.class)
class RedisCacheServiceTest {

    private static final String REDIS_PASSWORD = System.getenv("SPRING_DATA_REDIS_PASSWORD");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.2-alpine"))
            .withExposedPorts(6379)
            .withCommand("redis-server --requirepass " + REDIS_PASSWORD);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
        registry.add("spring.redis.password", () -> REDIS_PASSWORD);
    }

    @Autowired
    private RedisCacheService redisCacheService;

    @Test
    void 값을_저장하고_조회할_수_있다() {
        redisCacheService.setKeyAndValue("key1", "value1");
        String result = redisCacheService.getValueByKey("key1", String.class);
        assertThat(result).isEqualTo("value1");
    }

    @Test
    void TTL이_만료되면_값은_사라진다() throws InterruptedException {
        redisCacheService.setKeyAndValue("ttl:key", "value", Duration.ofSeconds(1));
        Thread.sleep(1500);
        String result = redisCacheService.getValueByKey("ttl:key", String.class);
        assertThat(result).isNull();
    }

    @Test
    void 키가_존재하면_exists는_true다() {
        redisCacheService.setKeyAndValue("exist:key", "value");
        assertThat(redisCacheService.isRefreshTokenExists("exist:key")).isTrue();
    }

    @Test
    void 키가_없으면_exists는_false다() {
        assertThat(redisCacheService.isRefreshTokenExists("not:exist")).isFalse();
    }

    @Test
    void 없는_키를_삭제해도_예외가_발생하지_않는다() {
        redisCacheService.deleteByKey("no:key");
        assertThat(redisCacheService.isRefreshTokenExists("no:key")).isFalse();
    }
}
