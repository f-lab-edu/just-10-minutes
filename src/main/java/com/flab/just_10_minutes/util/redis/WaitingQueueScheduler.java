package com.flab.just_10_minutes.util.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

import static com.flab.just_10_minutes.util.redis.command.RedisMultipleCommand.WAITING_KEY_PREFIX;

@Slf4j
@ConditionalOnProperty(value = "app.scheduling.enable", havingValue = "true", matchIfMissing = true)
@Component
@RequiredArgsConstructor
public class WaitingQueueScheduler {

    private final RedisService redisService;

    @Scheduled(fixedDelay = 1500)
    private void transferSchedule() {
        String pattern = WAITING_KEY_PREFIX;
        List<String> waitingQueues = redisService.fetchWaitingKeys(pattern + "*");
        for(String keys : waitingQueues) {
            String key = keys.split(":")[1];
            redisService.removeExpiredProcessingMember(key);
            redisService.transferWaitingToProcessing(key);
        }
    }
}
