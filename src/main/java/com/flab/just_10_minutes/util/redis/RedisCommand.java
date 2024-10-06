package com.flab.just_10_minutes.util.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import java.util.*;

import static com.flab.just_10_minutes.util.common.TimeUtil.convertMinutesToMilliSeconds;
import static com.flab.just_10_minutes.util.common.TimeUtil.fetchCurrentTimeMillis;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisCommand {

    private final StringRedisTemplate redisTemplate;

    @Value("${redis.command.ttl}")
    private Integer ttl;

    @Value("${redis.command.processing-threshold}")
    private Integer processingThreshold;

    private Long systemTTL;

    @PostConstruct
    public void initTTL() {
        this.systemTTL = convertMinutesToMilliSeconds(ttl);
    }

    public static final String WAITING_KEY_PREFIX = "WAITING:";
    public static final String PROCESSING_KEY_PREFIX = "PROCESSING:";
    public static final String FINISHING_KEY_PREFIX = "FINISHING:";

    public void deleteKey (String key) {
        redisTemplate.delete(key);
    }

    public Long fetchWaitingRank(String key, String member) {
        Long rank = redisTemplate.opsForZSet().rank(WAITING_KEY_PREFIX + key, member);
        if (rank == null) {
            return 0L;
        }
        return rank + 1L;
    }

    public Long addProcessingQueue(final String key, final String member) {
        if (!redisTemplate.opsForZSet().add(PROCESSING_KEY_PREFIX + key,
                                                member,
                                        fetchCurrentTimeMillis() + systemTTL)) {
            return null;
        }

        return fetchProcessingRank(key, member);
    }

    public Long fetchProcessingRank(String key, String member) {
        Long rank = redisTemplate.opsForZSet().rank(PROCESSING_KEY_PREFIX + key, member);
        if (rank == null) {
            return 0L;
        }
        return rank + 1L;
    }

    public Long allocateWaitingQueue(String key, String member) {
        String processingKey = PROCESSING_KEY_PREFIX + key;
        String waitingKey = WAITING_KEY_PREFIX + key;

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(
                "local key = KEYS[1] " +
                "local processingKey = ARGV[1] " +
                "local waitingKey = ARGV[2] " +
                "local member = ARGV[3] " +
                "local currentTime = tonumber(ARGV[4]) " +
                "local ttl = tonumber(ARGV[5]) " +
                "local threshold = tonumber(ARGV[6]) " +

                "local waitingQueueSize = redis.call('ZCARD', waitingKey) " +
                "local processingQueueSize = redis.call('ZCARD', processingKey) " +
                "if waitingQueueSize == 0 and processingQueueSize < threshold then " +
                "   local processingResult = redis.call('ZADD', processingKey, 'NX', currentTime + ttl, member) " +
                "   if processingResult == 0 then " +
                "       return nil " +
                "   end " +
                "   return 0 " +
                "end " +

                "local result = redis.call('ZADD', waitingKey, 'NX', ttl, member) " +
                "if result == 0 then " +
                "   return nil " +
                "end " +

                "local waitingRank = redis.call('ZRANK', waitingKey, member) " +
                "return waitingRank + 1"
        );
        redisScript.setResultType(Long.class);

        List<String> keys = Collections.singletonList(key);
        List<String> args = Arrays.asList(processingKey,
                                            waitingKey,
                                            member,
                                            String.valueOf(fetchCurrentTimeMillis()),
                                            String.valueOf(systemTTL),
                                            String.valueOf(processingThreshold));

        return redisTemplate.execute(redisScript, keys, args.toArray());
    }
}
