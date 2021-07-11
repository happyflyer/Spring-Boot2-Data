package org.example.spring_boot2.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
class RedisConnectionTest {
    @Resource
    RedisConnectionFactory redisConnectionFactory;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void testConnection() {
        log.info("redisConnectionFactory 类型是 {}", redisConnectionFactory.getClass().getName());
    }

    @Test
    void testOperation() {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set("hello", "world");
        log.info("获取到的数据：{}", operations.get("hello"));
    }
}
