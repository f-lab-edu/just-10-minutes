package com.flab.just_10_minutes.util.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

import static com.flab.just_10_minutes.util.common.TimeUtil.convertMinutesToMilliSeconds;
import static com.flab.just_10_minutes.util.common.TimeUtil.fetchCurrentTimeMillis;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisSingleCommand {

    private final StringRedisTemplate redisTemplate;

    @Value("${redis.command.ttl}")
    private Integer ttl;

    private Long systemTTL;

    @PostConstruct
    public void initTTL() {
        this.systemTTL = convertMinutesToMilliSeconds(ttl);
    }

    public static final String WAITING_KEY_PREFIX = "WAITING:";
    public static final String PROCESSING_KEY_PREFIX = "PROCESSING:";

    public void deleteKey(final String key) {
        redisTemplate.delete(key);
    }

    public Long fetchWaitingRank(final String key, final String member) {
        Long rank = redisTemplate.opsForZSet().rank(WAITING_KEY_PREFIX + key, member);
        if (rank == null) {
            return 0L;
        }
        return rank + 1L;
    }

    public List<String> fetchALlWaitingQueues(final String keyPattern) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(keyPattern).count(100).build();
        List<String> waitingQueue = new ArrayList<>();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                waitingQueue.add(cursor.next());
            }
        }

        return waitingQueue;
    }

    public Long fetchWaitingQueueSize(final String key) {
        return redisTemplate.opsForZSet().zCard(WAITING_KEY_PREFIX + key);
    }

    public Long addProcessingQueue(final String key, final String member) {
        if (!redisTemplate.opsForZSet().add(PROCESSING_KEY_PREFIX + key,
                                                member,
                                        fetchCurrentTimeMillis() + systemTTL)) {
            return null;
        }

        return fetchProcessingRank(key, member);
    }

    public Long addProcessingQueue(final String key, final String member, final Integer ttlMinutes) {
        if (!redisTemplate.opsForZSet().add(PROCESSING_KEY_PREFIX + key,
                                                member,
                                        fetchCurrentTimeMillis() + convertMinutesToMilliSeconds(ttlMinutes))) {
            return null;
        }

        return fetchProcessingRank(key, member);
    }

    public Long fetchProcessingRank(final String key, final String member) {
        Long rank = redisTemplate.opsForZSet().rank(PROCESSING_KEY_PREFIX + key, member);
        if (rank == null) {
            return 0L;
        }
        return rank + 1L;
    }

    public Long fetchProcessingQueueSize(final String key) {
        return redisTemplate.opsForZSet().zCard(PROCESSING_KEY_PREFIX + key);
    }

    public void removeExpiredProcessingMember(final String key) {
        redisTemplate.opsForZSet().removeRangeByScore(PROCESSING_KEY_PREFIX + key, -Double.MAX_VALUE, fetchCurrentTimeMillis());
    }

    public void removeProcessingValue(final String key, final String member) {
        redisTemplate.opsForZSet().remove(PROCESSING_KEY_PREFIX + key, member);
    }
}
