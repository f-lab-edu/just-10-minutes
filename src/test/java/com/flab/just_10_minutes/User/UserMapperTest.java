package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void findByLoginId_유저가_없는_경우() {
        //given
        final String longinId = "notExitsTestId";

        //when
        User user = userMapper.findByLoginId(longinId);

        //then
        assertThat(user).isNull();
    }

    @Test
    public void findByLoginId_유저가_존재하는_경우() {
        //given
        final String longinId = "existTestId";

        //when
        User user = userMapper.findByLoginId(longinId);

        //then
        assertThat(user.getLoginId()).isEqualTo("existTestId");
        assertThat(user.getPassword()).isEqualTo("testPassword");
        assertThat(user.getPhone()).isEqualTo("010-1234-5678");
        assertThat(user.getAddress()).isEqualTo("testAddress");
        assertThat(user.getRole()).isEqualTo(User.ROLE.PUBLIC);
    }

    @Test
    public void save_성공() {
        //given
        User user = User.builder()
                .loginId("testId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .build();

        //when
        int saveResult = userMapper.save(user);
        User saveUser = userMapper.findByLoginId(user.getLoginId());

        //then
        assertThat(saveResult).isEqualTo(1);
        assertThat(saveUser.getId()).isEqualTo(2);
    }
}
