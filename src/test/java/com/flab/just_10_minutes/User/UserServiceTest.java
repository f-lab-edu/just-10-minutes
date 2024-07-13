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

import static com.flab.just_10_minutes.User.UserTestFixture.*;
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
        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);

        //when
        Optional<User> existUser = target.findByLoginId(NOT_EXIST_ID);

        //then
        assertThat(existUser.isPresent()).isEqualTo(false);
    }

    @Test
    public void findByLoginId는_값이_있으면_null_이_아니다() {
        //given
        User user = createUser();

        doReturn(user).when(userMapper).findByLoginId(EXIST_ID);

        //when
        Optional<User> existUser = target.findByLoginId(EXIST_ID);

        //then
        assertThat(existUser.isPresent()).isEqualTo(true);
    }

    @Test
    public void validateExistedUser은_값이_null이면_false를_반환한다() {
        //given
        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);

        //when
        Boolean result = target.validateExistedUser(NOT_EXIST_ID);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void validateExistedUser은_값이_존재하면_true를_반환한다() {
        //given
        User user = createUser();

        doReturn(user).when(userMapper).findByLoginId(EXIST_ID);

        //when
        Boolean result = target.validateExistedUser(EXIST_ID);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void save는_이미_존재하는_회원이_있다면_실패한다() {
        //given
        User existUser = createUser();

        doReturn(existUser).when(userMapper).findByLoginId(EXIST_ID);

        //when
        //then
        assertThrows(RuntimeException.class, () -> target.save(existUser));
    }

    @Test
    public void save_성공() {
        //given
        User notExistUser = createUser(NOT_EXIST_ID);

        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);
        doReturn(1).when(userMapper).save(any(User.class));

        //when
        target.save(notExistUser);

        //then
        verify(userMapper, times(1)).findByLoginId(NOT_EXIST_ID);
        verify(userMapper, times(1)).save(any(User.class));
    }
}
