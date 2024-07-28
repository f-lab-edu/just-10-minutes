package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.User.service.UserService;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import com.flab.just_10_minutes.Util.Exception.Database.DatabaseException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static com.flab.just_10_minutes.User.UserDtoTestFixture.EXIST_ID;
import static com.flab.just_10_minutes.User.UserDtoTestFixture.NOT_EXIST_ID;
import static com.flab.just_10_minutes.User.UserTestFixture.*;
import static com.flab.just_10_minutes.Util.Exception.Business.BusinessException.DUPLICATED_REGISTER;
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
    public void save_실패_이미_존재하는_회원() {
        //given
        Optional<User> user = createOptionalUser(EXIST_ID);

        doReturn(true).when(userDao).existsByLoginId(EXIST_ID);

        //when
        final BusinessException result = assertThrows(BusinessException.class, () -> target.save(user.get()));

        //then
        assertThat(result.getMessage()).isEqualTo(DUPLICATED_REGISTER);
    }

    @Test
    public void save_성공() {
        //given
        Optional<User> notExistUser = createOptionalUser(NOT_EXIST_ID);
        User inputUser = createUser(NOT_EXIST_ID);

        doReturn(false).when(userDao).existsByLoginId(NOT_EXIST_ID);
        doNothing().when(userDao).save(any(User.class));

        //when
        target.save(inputUser);

        //then
        verify(userDao, times(1)).existsByLoginId(NOT_EXIST_ID);
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    public void getUserProfile_실패_찾는_대상이_없음() {
        //given
        doThrow(NotFoundException.class).when(userDao).fetch(NOT_EXIST_ID);

        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.getUserProfile(NOT_EXIST_ID));

        //then
        assertThat(result instanceof NotFoundException).isEqualTo(true);
    }

    @Test
    public void getUserProfile_성공() {
        //given
        User existUser = createUser(EXIST_ID);
        doReturn(existUser).when(userDao).fetch(EXIST_ID);

        //when
        target.getUserProfile(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
    }
}
