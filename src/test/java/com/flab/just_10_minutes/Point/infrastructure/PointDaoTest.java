package com.flab.just_10_minutes.Point.infrastructure;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.infrastructure.repository.PointHistoryDao;
import com.flab.just_10_minutes.Point.infrastructure.repository.PointHistoryMapper;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.repository.UserDao;
import com.flab.just_10_minutes.User.infrastructure.repository.UserMapper;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.List;

import static com.flab.just_10_minutes.Point.fixture.PointHistoryTestFixture.createPointHistory;
import static com.flab.just_10_minutes.User.fixture.UserDtoTestFixture.EXIST_ID;
import static com.flab.just_10_minutes.User.fixture.UserTestFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PointDaoTest {

    @Autowired
    private PointHistoryMapper pointHistoryMapper;

    @Autowired
    private UserMapper userMapper;

    private UserDao userDao;

    private PointHistoryDao target;

    @BeforeEach
    public void setUp() {
        userDao = new UserDao(userMapper);
        target = new PointHistoryDao(pointHistoryMapper);
    }

    public void saveUser(String loginId) {
        User user = createUser(loginId);
        userDao.save(user);
    }

    public void saveHistory(String loginId, Long quantity, String reason) {
        PointHistory initHistory = createPointHistory(loginId, quantity, reason, 0L);
        target.save(initHistory);
    }

//    @Test
//    public void save_성공() {
//        //given
//        PointHistory pointHistory = createPointHistory(EXIST_ID, 100L);
//
//        //when
//        target.save(pointHistory);
//        PointHistory latestHistory = target.findFirst(EXIST_ID).get();
//
//        //then
//        assertThat(latestHistory.getLoginId()).isEqualTo(pointHistory.getLoginId());
//        assertThat(latestHistory.getRequestQuantity()).isEqualTo(pointHistory.getRequestQuantity());
//        assertThat(latestHistory.getReason()).isEqualTo(pointHistory.getReason());
//    }

//    @Test
//    public void findFirst_null_반환_포인트_기록이_없음() {
//        //setUp
//        saveUser(EXIST_ID);
//        //given
//        //when
//        Optional<PointHistory> latestHistory = target.findFirst(EXIST_ID);
//
//        //then
//        assertThat(latestHistory.isPresent()).isEqualTo(false);
//    }

//    @Test
//    public void findFirst_PointHistory_객체_중_가장_최근에_저장된_기록을_반환() {
//        //setUp
//        saveUser(EXIST_ID);
//        PointHistory initHistory = createPointHistory(EXIST_ID, 100L, "first");
//        PointHistory secondHistory = createPointHistory(EXIST_ID, 100L, "second");
//        target.save(initHistory);
//        target.save(secondHistory);
//        //given
//        //when
//        PointHistory latestHistory = target.findFirst(EXIST_ID).get();
//
//        //then
//        assertThat(latestHistory).isEqualTo(secondHistory);
//    }

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
        PointHistory secondHistory = createPointHistory(EXIST_ID, 100L, "second");
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
