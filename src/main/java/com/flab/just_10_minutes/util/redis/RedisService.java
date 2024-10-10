package com.flab.just_10_minutes.util.redis;

import com.flab.just_10_minutes.util.exception.business.BusinessException;
import com.flab.just_10_minutes.util.redis.command.RedisMultipleCommand;
import com.flab.just_10_minutes.util.redis.command.RedisSingleCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisSingleCommand redisSingleCommand;
    private final RedisMultipleCommand redisMultipleCommand;

    public Long handleWaitingAllocation(final String key, final String member) {
        Long waitingRank = redisMultipleCommand.allocateWaitingQueue(key, member);

        if (waitingRank == null) {
            throw new RuntimeException("Internal Error Occur : Allocate Failed");
        }
        return waitingRank;
    }

    public List<String> fetchWaitingKeys(final String keyPattern) {
        return redisSingleCommand.fetchALlWaitingQueues(keyPattern);
    }

    public Long fetchWaitingNumber(final String key, final String member) {
        Long waitingRank = redisSingleCommand.fetchWaitingRank(String.valueOf(key), member);
        if (waitingRank == 0L) {
            Long processingRank = redisSingleCommand.fetchProcessingRank(String.valueOf(key), member);
            if (processingRank != 0L) {
                return 0L;
            }
            throw new BusinessException("Not Exist Waiting Number");
        } else {
            return waitingRank;
        }
    }

    public void removeExpiredProcessingMember(final String key) {
        redisSingleCommand.removeExpiredProcessingMember(key);
    }

    public void transferWaitingToProcessing (final String key) {
        redisMultipleCommand.transferWaitingToProcessing(key);
    }

    public void removeProcessingMember(final String key, final String member) {
        redisSingleCommand.removeProcessingValue(key, member);
    }
}
