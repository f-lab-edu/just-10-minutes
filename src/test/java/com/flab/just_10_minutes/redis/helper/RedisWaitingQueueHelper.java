package com.flab.just_10_minutes.redis.helper;

import org.springframework.data.redis.core.StringRedisTemplate;

import static com.flab.just_10_minutes.util.common.TimeUtil.convertMinutesToMilliSeconds;
import static com.flab.just_10_minutes.util.common.TimeUtil.fetchCurrentTimeMillis;
import static com.flab.just_10_minutes.util.redis.command.RedisSingleCommand.PROCESSING_KEY_PREFIX;

public class RedisWaitingQueueHelper implements WaitingQueueHelper {

    private StringRedisTemplate redisTemplate;

    public RedisWaitingQueueHelper(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void clean(String key) {
        deleteKey("WAITING:" + key);
        deleteKey("PROCESSING:" + key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void addFinishingQueue(String key, String member) {
        redisTemplate.opsForSet().add("FINISHING:" + key, member);
    }

    @Override
    public Long fetchFinishingQueue(String key) {
        return redisTemplate.opsForSet().size("FINISHING:" + key);
    }

    @Override
    public Long addProcessingQueue(final String key, final String member, final Integer ttlMinutes) {
        if (!redisTemplate.opsForZSet().add(PROCESSING_KEY_PREFIX + key,
                member,
                fetchCurrentTimeMillis() + convertMinutesToMilliSeconds(ttlMinutes))) {
            return null;
        }

        Long rank = redisTemplate.opsForZSet().rank(PROCESSING_KEY_PREFIX + key, member);
        if (rank == null) {
            return 0L;
        }
        return rank + 1L;
    }
}
