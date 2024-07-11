package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import com.flab.just_10_minutes.User.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService target;

    @Mock
    private UserMapper userMapper;

    @Test
    public void findByLoginId는_값이_없으면_null을_반환한다() {
        //given
        doReturn(null).when(userMapper).findByLoginId("testId");

        //when
        Optional<User> existUser = target.findByLoginId("testId");

        //then
        assertThat(existUser.isPresent()).isEqualTo(false);
    }

    @Test
    public void findByLoginId는_값이_있으면_null_이_아니다() {
        //given
        User user = User.builder()
                .loginId("testId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .build();

        doReturn(user).when(userMapper).findByLoginId("testId");

        //when
        Optional<User> existUser = target.findByLoginId("testId");

        //then
        assertThat(existUser.isPresent()).isEqualTo(true);
    }

    @Test
    public void validateExistedUser은_값이_null이면_false를_반환한다() {
        //given
        doReturn(null).when(userMapper).findByLoginId("testId");

        //when
        Boolean result = target.validateExistedUser("testId");

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void validateExistedUser은_값이_존재하면_true를_반환한다() {
        //given
        User user = User.builder()
                .loginId("testId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .build();

        doReturn(user).when(userMapper).findByLoginId("testId");

        //when
        Boolean result = target.validateExistedUser("testId");

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void save는_이미_존재하는_회원이_있다면_실패한다() {
        //given
        User user = User.builder()
                .loginId("testId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .build();

        doReturn(user).when(userMapper).findByLoginId("testId");

        //when
        //then
        assertThrows(RuntimeException.class, () -> target.save(user));
    }

    @Test
    public void save성공() {
        //given
        User user = User.builder()
                .loginId("testId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .build();
        doReturn(null).when(userMapper).findByLoginId("testId");
        doReturn(1).when(userMapper).save(any(User.class));

        //when
        target.save(user);

        //then
        verify(userMapper, times(1)).findByLoginId("testId");
        verify(userMapper, times(1)).save(any(User.class));
    }
}
