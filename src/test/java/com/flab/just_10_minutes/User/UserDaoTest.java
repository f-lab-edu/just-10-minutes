package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.persistence.UserDao;
import com.flab.just_10_minutes.User.persistence.UserMapper;
import com.flab.just_10_minutes.Util.ErrorResult.DaoErrorResult;
import com.flab.just_10_minutes.Util.ErrorResult.UserErrorResult;
import com.flab.just_10_minutes.Util.Exception.DaoException;
import com.flab.just_10_minutes.Util.Exception.UserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import java.util.Optional;

import static com.flab.just_10_minutes.User.UserTestFixture.*;
import static com.flab.just_10_minutes.User.UserTestFixture.EXIST_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @InjectMocks
    private UserDao target;

    @Mock
    private UserMapper userMapper;

    @Test
    public void save_실패_결과가_1이_아님() {
        //given
        User user = createUser();

        doReturn(2).when(userMapper).save(user);

        //when
        final DaoException result = assertThrows(DaoException.class, () -> target.save(user));

        //then
        assertThat(result.getErrorResult()).isEqualTo(DaoErrorResult.INSERT_ERROR);
    }

    @Test
    public void save_실패_중복되는_loginId() {
        //given
        User user = createUser();

        doThrow(DuplicateKeyException.class).when(userMapper).save(user);

        //when
        final UserException result = assertThrows(UserException.class, () -> target.save(user));

        //then
        assertThat(result.getErrorResult()).isEqualTo(UserErrorResult.DUPLICATED_USER_REGISTER);
    }

    @Test
    public void save_성공() {
        //given
        User user = createUser();

        doReturn(1).when(userMapper).save(any(User.class));

        //when
        target.save(user);

        //then
        verify(userMapper, times(1)).save(any(User.class));
    }

    @Test
    public void findByLoginId_null_반환_존재하지_않는_회원() {
        //given
        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);

        //when
        Optional<User> existUser = target.findByLoginId(NOT_EXIST_ID);

        //then
        assertThat(existUser.isPresent()).isEqualTo(false);
    }

    @Test
    public void findByLoginId는_User_객체_반환_존재하는_회원() {
        //given
        User user = createUser();

        doReturn(user).when(userMapper).findByLoginId(EXIST_ID);

        //when
        Optional<User> existUser = target.findByLoginId(EXIST_ID);

        //then
        assertThat(existUser.isPresent()).isEqualTo(true);
    }
}
