package com.flab.just_10_minutes.redis;

import com.flab.just_10_minutes.redis.config.WaitingQueueTestConfiguration;
import com.flab.just_10_minutes.redis.helper.WaitingQueueHelper;
import com.flab.just_10_minutes.util.redis.command.RedisSingleCommand;
import com.flab.just_10_minutes.util.redis.command.RedisMultipleCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "app.scheduling.enable=false",
        "redis.command.ttl=3",
        "redis.command.processing-threshold=3"
})
@Import(WaitingQueueTestConfiguration.class)
@Slf4j
public class RedisCommandTest {

    @Autowired
    private RedisSingleCommand redisSingleCommand;

    @Autowired
    private RedisMultipleCommand redisMultipleCommand;

    @Autowired
    private WaitingQueueHelper waitingQueueHelper;

    @Value("${redis.command.ttl}")
    private Integer ttl;


    public static final String productId = "product1";

    public static final String userPrefix = "user";

    @BeforeEach
    public void setUp() {
        waitingQueueHelper.clean(productId);
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold에 도달하지 않았을 때, " +
                "WaitingQueue가 비어있다면 WaitingQueue에 저장할 수 없다.(0을 반환한다.)")
    public void allocateWaitingQueueTest1() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);

        //when
        Long waitingResult = redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 3);

        Long waitingRank = redisSingleCommand.fetchWaitingRank(productId, userPrefix + 3);

        //then
        assertThat(waitingResult).isEqualTo(0L);
        assertThat(waitingRank).isEqualTo(0L);
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold에 도달 했을 때, " +
                "WaitingQueue가 비어있다면 WaitingQueue에 저장하고 순서를 반환한다.(순서는 1부터 시작한다.)")
    public void allocateWaitingQueueTest2() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);

        //when
        Long waitingResult = redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 3);

        Long waitingRank = redisSingleCommand.fetchWaitingRank(productId, userPrefix + 3);

        //then
        assertThat(waitingResult).isEqualTo(1L);
        assertThat(waitingRank).isEqualTo(1L);
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold에 도달 했을 때, " +
                "WaitingQueue의 크기가 0보다 크다면 WaitingQueue에 저장되고 순서를 반환한다.")
    public void allocateWaitingQueueTest3() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);
        redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 4);

        //when
        Long waitingResult = redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 5);

        Long waitingRank = redisSingleCommand.fetchWaitingRank(productId, userPrefix + 5);

        //then
        assertThat(waitingResult).isEqualTo(2L);
        assertThat(waitingRank).isEqualTo(2L);
    }

    @Test
    @DisplayName("ProcessingQueue가 비어있고 WaitingQueue의 크기가 0보다 크다면, " +
                "WaitingQueue에 저장되고 순서를 반환한다.")
    public void allocateWaitingQueueTest4() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);

        redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 4);
        redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 5);

        //when
        Long waitingResult = redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 6);

        Long waitingRank = redisSingleCommand.fetchWaitingRank(productId, userPrefix + 6);

        //then
        assertThat(waitingResult).isEqualTo(3L);
        assertThat(waitingRank).isEqualTo(3L);
    }

    @Test
    @DisplayName("ProcessingQueue에 ttl이 만료된 member는 삭제된다.")
    public void removeExpiredProcessingMemberTest() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, 0);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, 0);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, 0);

        //when
        redisSingleCommand.removeExpiredProcessingMember(productId);

        //then
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(processingQueueSize).isEqualTo(0L);
    }

    @Test
    @DisplayName("ProcessingQueue에 ttl이 만료되지 않은 member는 삭제되지 않는다..")
    public void removeExpiredProcessingMemberTest2() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, 3);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, 3);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, 3);

        //when
        redisSingleCommand.removeExpiredProcessingMember(productId);

        //then
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(processingQueueSize).isEqualTo(3L);
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold보다 크면 false를 반환한다.")
    public void transferWaitingToProcessingTest1() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 4, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 5, ttl);

        //when
        Boolean result = redisMultipleCommand.transferWaitingToProcessing(productId);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold와 같을 때 WaitingQueue의 가 비어있으면 false를 반환한다. " +
                "이 때 WaitingQueue에서 ProcessingQueue의 변화는 없다.")
    public void transferWaitingToProcessingTest2() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);

        //when
        Boolean result = redisMultipleCommand.transferWaitingToProcessing(productId);

        //then
        Long waitingQueueSize = redisSingleCommand.fetchWaitingQueueSize(productId);
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(waitingQueueSize).isEqualTo(0L);
        assertThat(processingQueueSize).isEqualTo(3L);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold와 같을 때 WaitingQueue의 크기가 0보다 크면 false를 반환한다. " +
                "이 때 WaitingQueue에서 ProcessingQueue의 변화는 없다.")
    public void transferWaitingToProcessingTest3() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);

        redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 4);

        //when
        Boolean result = redisMultipleCommand.transferWaitingToProcessing(productId);

        //then
        Long waitingQueueSize = redisSingleCommand.fetchWaitingQueueSize(productId);
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(waitingQueueSize).isEqualTo(1L);
        assertThat(processingQueueSize).isEqualTo(3L);
        assertThat(result).isFalse();
    }


    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold보다 작아지면 true를 반환한다. " +
                "이 때 hreshold - WaitingQueue.size 만큼 value를 WaitingQueue 에서 ProcessingQueue로 옮긴다.")
    public void transferWaitingToProcessingTest4() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 3, ttl);

        redisMultipleCommand.allocateWaitingQueue(productId, userPrefix + 4);
        redisSingleCommand.removeProcessingValue(productId, userPrefix + 1);

        //when
        Boolean result = redisMultipleCommand.transferWaitingToProcessing(productId);

        //then
        Long waitingQueueSize = redisSingleCommand.fetchWaitingQueueSize(productId);
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(waitingQueueSize).isEqualTo(0L);
        assertThat(processingQueueSize).isEqualTo(3L);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 0보다 크면서 WaitingQueue가 비어있으면 false를 반환한다. " +
                "이 때 WaitingQueue에서 ProcessingQueue의 변화는 없다.")
    public void transferWaitingToProcessingTest5() {
        //given
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 1, ttl);
        waitingQueueHelper.addProcessingQueue(productId, userPrefix + 2, ttl);

        //when
        Boolean result = redisMultipleCommand.transferWaitingToProcessing(productId);

        //then
        Long waitingQueueSize = redisSingleCommand.fetchWaitingQueueSize(productId);
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(waitingQueueSize).isEqualTo(0L);
        assertThat(processingQueueSize).isEqualTo(2L);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("ProcessingQueue와 WaitingQueue가 모두 비어있으면 false를 반환한다. " +
                "이 때 WaitingQueue에서 ProcessingQueue의 변화는 없다.")
    public void transferWaitingToProcessingTest6() {
        //given

        //when
        Boolean result = redisMultipleCommand.transferWaitingToProcessing(productId);

        //then
        Long waitingQueueSize = redisSingleCommand.fetchWaitingQueueSize(productId);
        Long processingQueueSize = redisSingleCommand.fetchProcessingQueueSize(productId);
        assertThat(waitingQueueSize).isEqualTo(0L);
        assertThat(processingQueueSize).isEqualTo(0L);
        assertThat(result).isFalse();
    }
}
