package com.flab.just_10_minutes.user.infrastructure;

import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import com.flab.just_10_minutes.user.infrastructure.repository.UserMapper;
import com.flab.just_10_minutes.util.exception.database.DatabaseException;
import com.flab.just_10_minutes.util.exception.database.DuplicatedKeyException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.Optional;

import static com.flab.just_10_minutes.user.fixture.UserDtoTestFixture.EXIST_ID;
import static com.flab.just_10_minutes.user.fixture.UserDtoTestFixture.NOT_EXIST_ID;
import static com.flab.just_10_minutes.user.fixture.UserTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDaoTest {

    @Autowired
    private UserMapper userMapper;

    private UserDao target;

    @BeforeEach
    public void setUp() {
        target = new UserDao(userMapper);
    }

    public void saveUser(String loginId) {
        User user = createUser(loginId);
        target.save(user);
    }

    @Test
    public void save_실패_중복키() {
        //setUp
        saveUser(EXIST_ID);
        //given
        User user = createUser(EXIST_ID);

        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.save(user));

        //then
        assertThat(result instanceof DuplicatedKeyException).isEqualTo(true);
    }

    @Test
    public void save_성공() {
        //given
        User user = createUser(NOT_EXIST_ID);

        //when
        target.save(user);
        boolean result = target.existsByLoginId(NOT_EXIST_ID);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void findByLoginId_null_반환_존재하지_않는_회원() {
        //given
        //when
        Optional<UserEntity> existUser = target.findByLoginId(NOT_EXIST_ID);

        //then
        assertThat(existUser.isPresent()).isEqualTo(false);
    }

    @Test
    public void findByLoginId_User_객체_반환_존재하는_회원() {
        //setUp
        saveUser(EXIST_ID);
        //given
        //when
        Optional<UserEntity> existUser = target.findByLoginId(EXIST_ID);

        //then
        assertThat(existUser.isPresent()).isEqualTo(true);
    }

    @Test
    public void existsById_false_반환_찾는_대상이_없음() {
        //given
        //when
        boolean result = target.existsByLoginId(NOT_EXIST_ID);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void existsById_true_반환_찾는_대상이_있음() {
        //setUp
        saveUser(EXIST_ID);
        //given
        //when
        boolean result = target.existsByLoginId(EXIST_ID);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void fetch_실패_존재하지_않는_회원() {
        //given
        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.fetch(NOT_EXIST_ID));

        //then
        assertThat(result instanceof NotFoundException).isEqualTo(true);
    }

    @Test
    public void fetch_성공() {
        //setUp
        saveUser(EXIST_ID);
        //given
        //when
        User existUser = target.fetch(EXIST_ID);

        //then
        assertThat(existUser != null).isEqualTo(true);
    }

    @Test
    public void patchPoints_성공() {
        //setUp
        saveUser(EXIST_ID);
        //given
        //when
        target.patchPoint(EXIST_ID, 1000L);

        Optional<UserEntity> existUser = target.findByLoginId(EXIST_ID);

        //then
        assertThat(existUser.get().getPoint()).isEqualTo(1000L);
    }
}
