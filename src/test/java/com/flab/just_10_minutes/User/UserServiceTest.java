package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.persistence.UserDao;
import com.flab.just_10_minutes.User.service.UserService;
import com.flab.just_10_minutes.Util.ErrorResult.UserErrorResult;
import com.flab.just_10_minutes.Util.Exception.UserException;
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
    private UserDao userDao;

    private Optional<User> createOptionalUser(String loginId) {
        if (loginId == null) {
            throw new RuntimeException("loginId must not be null");
        }

        if (!EXIST_ID.equals(loginId) && !NOT_EXIST_ID.equals(loginId)) {
            throw new RuntimeException("loginId must be EXIST_ID or NOT_EXIST_ID");
        }

        if (EXIST_ID.equals(loginId)) {
            return Optional.ofNullable(createUser(loginId));
        } else {
            return Optional.ofNullable(null);
        }
    }

    @Test
    public void validateExistedUser_false_반환_존재하지_않는_회원() {
        //given
        Optional<User> user = createOptionalUser(NOT_EXIST_ID);

        doReturn(user).when(userDao).findByLoginId(NOT_EXIST_ID);

        //when
        Boolean result = target.validateExistedUser(NOT_EXIST_ID);

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void validateExistedUser_true_반환_존재하는_회원() {
        //given
        Optional<User> user = createOptionalUser(EXIST_ID);

        doReturn(user).when(userDao).findByLoginId(EXIST_ID);

        //when
        Boolean result = target.validateExistedUser(EXIST_ID);

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void save_실패_이미_존재하는_회원() {
        //given
        Optional<User> user = createOptionalUser(EXIST_ID);

        doReturn(user).when(userDao).findByLoginId(EXIST_ID);

        //when
        final UserException result = assertThrows(UserException.class, () -> target.save(user.get()));

        //then
        assertThat(result.getErrorResult()).isEqualTo(UserErrorResult.DUPLICATED_USER_REGISTER);
    }

    @Test
    public void save_성공() {
        //given
        Optional<User> notExistUser = createOptionalUser(NOT_EXIST_ID);
        User inputUser = createUser(NOT_EXIST_ID);

        doReturn(notExistUser).when(userDao).findByLoginId(NOT_EXIST_ID);
        doNothing().when(userDao).save(any(User.class));
        //TODO : 아무것도 리턴 안하면 모킹을 안하는지? 혹은 doNothing으로 명시 해주는지? 질문
        //when
        target.save(inputUser);

        //then
        verify(userDao, times(1)).findByLoginId(NOT_EXIST_ID);
        verify(userDao, times(1)).save(any(User.class));
    }
}
