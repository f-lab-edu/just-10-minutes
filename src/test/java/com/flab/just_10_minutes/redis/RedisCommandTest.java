package com.flab.just_10_minutes.redis;

import com.flab.just_10_minutes.util.redis.RedisCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "app.scheduling.enable=false",
        "redis.command.ttl=3",
        "redis.command.processing-threshold=3"
})
@Slf4j
public class RedisCommandTest {

    @Autowired
    private RedisCommand redisCommand;

    public static final String productId = "product1";

    public static final String userPrefix = "user";

    @BeforeEach
    public void deleteKey() {
        redisCommand.deleteKey("WAITING:" + productId);
        redisCommand.deleteKey("PROCESSING:" + productId);
    }

    @Test
    @DisplayName("ProcessingQueue를 member를 입력하면 해당 key에 입력된 순서를 반환한다.")
    public void addProcessingQueueTest1() {
        //given
        //when
        Long user1Rank = redisCommand.addProcessingQueue(productId, userPrefix + 1);
        Long user2Rank = redisCommand.addProcessingQueue(productId, userPrefix + 2);

        //then
        assertThat(user1Rank).isEqualTo(1);
        assertThat(user2Rank).isEqualTo(2);
    }

    @Test
    @DisplayName("ProcessingQueue에 입력된 첫 번째 멤버의 순서는 1이다.")
    public void addProcessingQueueTest2() {
        //given
        //when
        Long user1Rank = redisCommand.addProcessingQueue(productId, userPrefix + 1);

        //then
        assertThat(user1Rank).isEqualTo(1);
    }

    @Test
    @DisplayName("ProcessingQueue에 중복된 value를 저장하면 null을 반환한다.")
    public void addProcessingQueueTest3() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1);
        redisCommand.addProcessingQueue(productId, userPrefix + 2);
        redisCommand.addProcessingQueue(productId, userPrefix + 3);

        //when
        Long waitingResult = redisCommand.addProcessingQueue(productId, userPrefix + 3);

        //then
        assertThat(waitingResult).isNull();
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold에 도달하지 않았을 때, " +
                "WaitingQueue가 비어있다면 WaitingQueue에 저장할 수 없다.(0을 반환한다.)")
    public void allocateWaitingQueueTest1() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1);
        redisCommand.addProcessingQueue(productId, userPrefix + 2);

        //when
        Long waitingResult = redisCommand.allocateWaitingQueue(productId, userPrefix + 3);

        Long waitingRank = redisCommand.fetchWaitingRank(productId, userPrefix + 3);

        //then
        assertThat(waitingResult).isEqualTo(0L);
        assertThat(waitingRank).isEqualTo(0L);
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold에 도달 했을 때, " +
                "WaitingQueue가 비어있다면 WaitingQueue에 저장하고 순서를 반환한다.(순서는 1부터 시작한다.)")
    public void allocateWaitingQueueTest2() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1);
        redisCommand.addProcessingQueue(productId, userPrefix + 2);
        redisCommand.addProcessingQueue(productId, userPrefix + 3);

        //when
        Long waitingResult = redisCommand.allocateWaitingQueue(productId, userPrefix + 3);

        Long waitingRank = redisCommand.fetchWaitingRank(productId, userPrefix + 3);

        //then
        assertThat(waitingResult).isEqualTo(1L);
        assertThat(waitingRank).isEqualTo(1L);
    }

    @Test
    @DisplayName("ProcessingQueue의 크기가 threshold에 도달 했을 때, " +
                "WaitingQueue의 크기가 0보다 크다면 WaitingQueue에 저장되고 순서를 반환한다.")
    public void allocateWaitingQueueTest3() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1);
        redisCommand.addProcessingQueue(productId, userPrefix + 2);
        redisCommand.addProcessingQueue(productId, userPrefix + 3);
        redisCommand.allocateWaitingQueue(productId, userPrefix + 4);

        //when
        Long waitingResult = redisCommand.allocateWaitingQueue(productId, userPrefix + 5);

        Long waitingRank = redisCommand.fetchWaitingRank(productId, userPrefix + 5);

        //then
        assertThat(waitingResult).isEqualTo(2L);
        assertThat(waitingRank).isEqualTo(2L);
    }

    @Test
    @DisplayName("ProcessingQueue가 비어있고 WaitingQueue의 크기가 0보다 크다면, " +
                "WaitingQueue에 저장되고 순서를 반환한다.")
    public void allocateWaitingQueueTest4() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1);
        redisCommand.addProcessingQueue(productId, userPrefix + 2);
        redisCommand.addProcessingQueue(productId, userPrefix + 3);

        redisCommand.allocateWaitingQueue(productId, userPrefix + 4);
        redisCommand.allocateWaitingQueue(productId, userPrefix + 5);

        //when
        Long waitingResult = redisCommand.allocateWaitingQueue(productId, userPrefix + 6);

        Long waitingRank = redisCommand.fetchWaitingRank(productId, userPrefix + 6);


        //then
        assertThat(waitingResult).isEqualTo(3L);
        assertThat(waitingRank).isEqualTo(3L);
    }

    @Test
    @DisplayName("WaitingQueue에 중복된 value를 저장하면 null을 반환한다.")
    public void allocateWaitingQueueTest5() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1);
        redisCommand.addProcessingQueue(productId, userPrefix + 2);
        redisCommand.addProcessingQueue(productId, userPrefix + 3);

        redisCommand.allocateWaitingQueue(productId, userPrefix + 4);
        redisCommand.allocateWaitingQueue(productId, userPrefix + 5);

        //when
        Long waitingResult = redisCommand.allocateWaitingQueue(productId, userPrefix + 5);

        //then
        assertThat(waitingResult).isNull();
    }

    @Test
    @DisplayName("ProcessingQueue에 ttl이 만료된 member는 삭제된다.")
    public void removeExpiredProcessingMemberTest() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1, 0);
        redisCommand.addProcessingQueue(productId, userPrefix + 2, 0);
        redisCommand.addProcessingQueue(productId, userPrefix + 3, 0);

        //when
        redisCommand.removeExpiredProcessingMember(productId);

        //then
        Long processingQueueSize = redisCommand.fetchProcessingQueueSize(productId);
        assertThat(processingQueueSize).isEqualTo(0L);
    }

    @Test
    @DisplayName("ProcessingQueue에 ttl이 만료되지 않은 member는 삭제되지 않는다..")
    public void removeExpiredProcessingMemberTest2() {
        //given
        redisCommand.addProcessingQueue(productId, userPrefix + 1, 3);
        redisCommand.addProcessingQueue(productId, userPrefix + 2, 3);
        redisCommand.addProcessingQueue(productId, userPrefix + 3, 3);

        //when
        redisCommand.removeExpiredProcessingMember(productId);

        //then
        Long processingQueueSize = redisCommand.fetchProcessingQueueSize(productId);
        assertThat(processingQueueSize).isEqualTo(3L);
    }
}
