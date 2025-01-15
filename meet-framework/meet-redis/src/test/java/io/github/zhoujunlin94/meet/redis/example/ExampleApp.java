package io.github.zhoujunlin94.meet.redis.example;

import io.github.zhoujunlin94.meet.redis.annotation.EnableRedisPubSub;
import io.github.zhoujunlin94.meet.redis.annotation.EnableRedissonDelayedQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhoujunlin
 * @date 2024-04-18-17:48
 */
// @EnableRedisQueue
@EnableRedissonDelayedQueue
@EnableRedisPubSub
@SpringBootApplication
public class ExampleApp {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApp.class);
    }

}
