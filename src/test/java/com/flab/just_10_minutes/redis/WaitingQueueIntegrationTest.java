package com.flab.just_10_minutes.redis;

import com.flab.just_10_minutes.redis.config.WaitingQueueTestConfiguration;
import com.flab.just_10_minutes.redis.helper.WaitingQueueHelper;
import com.flab.just_10_minutes.util.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "app.scheduling.enable=true",
        "redis.command.ttl=1",
        "redis.command.processing-threshold=30"
})
@Import(WaitingQueueTestConfiguration.class)
@Slf4j
public class WaitingQueueIntegrationTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private WaitingQueueHelper waitingQueueHelper;

    private final String productId = "product1";
    private final String userPrefix = "user";

    Deque<String> processingKeys = new ConcurrentLinkedDeque<>();
    Deque<String> waitingKeys = new ConcurrentLinkedDeque<>();
    Set<String> waitingValues = ConcurrentHashMap.newKeySet();

    @BeforeEach
    public void setUp() {
        waitingQueueHelper.clean(productId);
    }

    private void setUpQueues(Long numbersOfUser) {
        for(int i = 0; i < numbersOfUser; i++) {
            String userId = userPrefix + (i + 1);
            Long result = redisService.handleWaitingAllocation(productId, userId);
            if (result == 0L) {
                processingKeys.add(userId);
            } else {
                waitingKeys.add(userId);
                waitingValues.add(userId);
            }
        }
    }

    private void executingOrder(String productId, String userId) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Purchased Product : " + productId + " By User : " + userId);
        redisService.removeProcessingMember(productId, userId);
        waitingQueueHelper.addFinishingQueue(productId, userId);
    }

    @Test
    public void test2() throws InterruptedException {
        Long numberOfMember = 100L;
        setUpQueues(numberOfMember);

        int processingSize = processingKeys.size();
        ExecutorService processingExecutors = Executors.newFixedThreadPool(processingSize);
        CountDownLatch processingLatch = new CountDownLatch(processingSize);

        for(int i = 0 ; i < processingSize; i++) {
            processingExecutors.submit(() -> {
                try {
                    String userId = processingKeys.poll();
                    executingOrder(productId, userId);
                } catch (Exception e) {
                    log.info(e.getMessage());
                } finally {
                    processingLatch.countDown();
                }
            });
        }

        int waitingSize = waitingKeys.size();
        ExecutorService waitingExecutors = Executors.newFixedThreadPool(waitingSize);
        CountDownLatch waitingLatch = new CountDownLatch(waitingSize);

        for(int i = 0 ; i < waitingSize; i++) {
            waitingExecutors.submit(() -> {
                try {
                    String userId = waitingKeys.poll();

                    while (waitingValues.contains(userId)) {
                        Long waitingNumber = redisService.fetchWaitingNumber(productId, userId);
                        if (waitingNumber == 0L) {
                            executingOrder(productId, userId);
                            waitingValues.remove(userId);
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally{
                    waitingLatch.countDown();
                }
            });
        }

        processingLatch.await();
        waitingLatch.await();

        Long finishingSize = waitingQueueHelper.fetchFinishingQueue(productId);

        assertThat(finishingSize).isEqualTo(numberOfMember);
    }
}
