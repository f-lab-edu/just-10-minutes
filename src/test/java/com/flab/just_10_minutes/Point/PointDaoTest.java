package com.flab.just_10_minutes.Point;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.infrastructure.PointDao;
import com.flab.just_10_minutes.Point.infrastructure.PointMapper;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.User.infrastructure.UserMapper;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.List;
import java.util.Optional;

import static com.flab.just_10_minutes.Point.PointHistoryTestFixture.createPointHistory;
import static com.flab.just_10_minutes.User.UserDtoTestFixture.EXIST_ID;
import static com.flab.just_10_minutes.User.UserDtoTestFixture.NOT_EXIST_ID;
import static com.flab.just_10_minutes.User.UserTestFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PointDaoTest {

    @Autowired
    private PointMapper pointMapper;

    @Autowired
    private UserMapper userMapper;

    private UserDao userDao;

    private PointDao target;

    @BeforeEach
    public void setUp() {
        userDao = new UserDao(userMapper);
        target = new PointDao(pointMapper);
    }

    public void saveUser(String loginId) {
        User user = createUser(loginId);
        userDao.save(user);
    }

    public void saveHistory(String loginId, Long quantity, String reason) {
        PointHistory initHistory = createPointHistory(loginId, quantity, reason, 0L);
        target.save(target.calculateTotalQuantity(initHistory).get());
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("calculateTotalQuantity 테스트 : 포인트 추가")
    class PointHistoryDomainTestPLUS {

        @Test
        @DisplayName("최초 기록이 없는 경우")
        public void initHistory() {
            //setUp
            saveUser(NOT_EXIST_ID);

            //given
            PointHistory initHistory = createPointHistory(NOT_EXIST_ID, 100L, "initOffer", 0L);
            PointHistory expectedHistory = createPointHistory(NOT_EXIST_ID, 100L, "initOffer", 100L);

            //when
            Optional<PointHistory> pointHistory = target.calculateTotalQuantity(initHistory);

            //then
            assertThat(pointHistory.get()).isEqualTo(expectedHistory);
        }

        @Test
        @DisplayName("기록이 있는 경우")
        public void existHistory() {
            //setUp
            saveUser(NOT_EXIST_ID);
            saveHistory(NOT_EXIST_ID, 100L, "initOffer");

            //given
            PointHistory newHistory = createPointHistory(NOT_EXIST_ID, 200L, "secondOffer", 0L);

            PointHistory expectedHistory = createPointHistory(NOT_EXIST_ID, 200L, "secondOffer", 300L);

            //when
            Optional<PointHistory> pointHistory = target.calculateTotalQuantity(newHistory);

            //then
            assertThat(pointHistory.get()).isEqualTo(expectedHistory);
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("calculateTotalQuantity 테스트 : 포인트 차감")
    class PointHistoryDomainTestMINUS {

        @Test
        @DisplayName("최초 기록이 없는 경우")
        public void initHistory() {
            //setUp
            saveUser(NOT_EXIST_ID);

            //given
            PointHistory initHistory = createPointHistory(NOT_EXIST_ID, -100L, "initOffer", 0L);

            //when
            Optional<PointHistory> pointHistory = target.calculateTotalQuantity(initHistory);

            //then
            assertThat(pointHistory.isPresent()).isEqualTo(false);
        }

        @Test
        @DisplayName("기록된 포인트가 있고 차감할 포인트 < 보유한 포인트")
        public void existHistory1() {
            //setUp
            saveUser(NOT_EXIST_ID);
            saveHistory(NOT_EXIST_ID, 100L, "initOffer");

            //given
            PointHistory initHistory = createPointHistory(NOT_EXIST_ID, -200L, "secondOffer", 0L);

            PointHistory expectedHistory = createPointHistory(NOT_EXIST_ID, -100L, "secondOffer", 0L);

            //when
            Optional<PointHistory> pointHistory = target.calculateTotalQuantity(initHistory);

            //then
            assertThat(pointHistory.get()).isEqualTo(expectedHistory);
        }

        @Test
        @DisplayName("포인트 차감 기록된 포인트가 있고 차감할 포인트 == 보유한 포인트")
        public void existHistory2() {
            //setUp
            saveUser(NOT_EXIST_ID);
            saveHistory(NOT_EXIST_ID, 100L, "initOffer");

            //given
            PointHistory initHistory = createPointHistory(NOT_EXIST_ID, -100L, "secondOffer", 0L);

            PointHistory expectedHistory = createPointHistory(NOT_EXIST_ID, -100L, "secondOffer", 0L);

            //when
            Optional<PointHistory> pointHistory = target.calculateTotalQuantity(initHistory);

            //then
            assertThat(pointHistory.get()).isEqualTo(expectedHistory);
        }


        @Test
        @DisplayName("포인트 차감 기록된 포인트가 있고 차감할 포인트 < 보유한 포인트")
        public void existHistory3() {
            //setUp
            saveUser(NOT_EXIST_ID);
            saveHistory(NOT_EXIST_ID, 500L, "initOffer");

            //given
            PointHistory initHistory = createPointHistory(NOT_EXIST_ID, -100L, "secondOffer", 0L);

            PointHistory expectedHistory = createPointHistory(NOT_EXIST_ID, -100L, "secondOffer", 400L);

            //when
            Optional<PointHistory> pointHistory = target.calculateTotalQuantity(initHistory);

            //then
            assertThat(pointHistory.get()).isEqualTo(expectedHistory);
        }
    }

    @Test
    public void save_성공() {
        //given
        PointHistory pointHistory = createPointHistory(EXIST_ID, 100L);

        //when
        target.save(pointHistory);
        PointHistory latestHistory = target.findTopByOrderByLoginIdDesc(EXIST_ID);

        //then
        assertThat(latestHistory.getLoginId()).isEqualTo(pointHistory.getLoginId());
        assertThat(latestHistory.getQuantity()).isEqualTo(pointHistory.getQuantity());
        assertThat(latestHistory.getReason()).isEqualTo(pointHistory.getReason());
    }

    @Test
    public void findTopByOrderByLoginIdDesc_null_반환_포인트_기록이_없음() {
        //given
        //when
        PointHistory latestHistory = target.findTopByOrderByLoginIdDesc(EXIST_ID);

        //then
        assertThat(latestHistory).isNull();
    }

    @Test
    public void findTopByOrderByLoginIdDesc_PointHistory_객체_중_가장_최근에_저장된_기록을_반환() {
        //given
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L, "first");
        PointHistory secondHistory = createPointHistory(EXIST_ID, 100L, "second").calculateTotalQuantity(initHistory);
        target.save(initHistory);
        target.save(secondHistory);

        //when
        PointHistory latestHistory = target.findTopByOrderByLoginIdDesc(EXIST_ID);

        //then
        assertThat(latestHistory).isEqualTo(secondHistory);
    }

    @Test
    public void findByLoginId_빈_리스트_반환_포인트_기록이_없음() {
        //given
        //when
        List<PointHistory> histories = target.findByLoginId(EXIST_ID);

        //then
        assertThat(histories.size()).isEqualTo(0);
    }

    @Test
    public void findByLoginId_성공() {
        //given
        //setup
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L, "first");
        PointHistory secondHistory = createPointHistory(EXIST_ID, 100L, "second").calculateTotalQuantity(initHistory);
        target.save(initHistory);
        target.save(secondHistory);

        //when
        List<PointHistory> histories = target.findByLoginId(EXIST_ID);

        //then
        assertThat(histories.size()).isEqualTo(2);
        assertThat(histories.get(0)).isEqualTo(initHistory);
        assertThat(histories.get(1)).isEqualTo(secondHistory);
    }
}
