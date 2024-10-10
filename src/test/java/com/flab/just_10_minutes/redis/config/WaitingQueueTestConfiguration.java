package com.flab.just_10_minutes.redis.config;

import com.flab.just_10_minutes.redis.helper.RedisWaitingQueueHelper;
import com.flab.just_10_minutes.redis.helper.WaitingQueueHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
public class WaitingQueueTestConfiguration {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Bean
    public WaitingQueueHelper waitingQueueHelper() {
        return new RedisWaitingQueueHelper(redisTemplate);
    }
}
