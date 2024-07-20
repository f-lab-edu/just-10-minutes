package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.persistence.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import static com.flab.just_10_minutes.User.UserTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MybatisTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByLoginId_null_반환_존재하지_않는_회원() {
        //given
        //when
        User user = userMapper.findByLoginId(NOT_EXIST_ID);

        //then
        assertThat(user).isNull();
    }

    @Test
    public void findByLoginId_성공() {
        //given
        User existUser = createUser();

        //when
        User user = userMapper.findByLoginId(EXIST_ID);

        //then
        assertThat(user.equals(existUser)).isEqualTo(true);
    }

    @Test
    public void save_실패_이미_존재하는_회원() {
        //given
        User existUser = createUser();

        //when
        //then
        assertThrows(DuplicateKeyException.class, () -> userMapper.save(existUser));
    }

    @Test
    public void save_성공() {
        //given
        User newUser = createUser("newId");

        //when
        int saveResult = userMapper.save(newUser);
        User saveUser = userMapper.findByLoginId(newUser.getLoginId());

        //then
        assertThat(saveResult).isEqualTo(1);
        assertThat(saveUser.getId()).isEqualTo(2);
    }
}
