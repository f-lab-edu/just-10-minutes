package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.mapper.PointMapper;
import com.flab.just_10_minutes.Point.service.PointService;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.dto.UserInfoDto;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import com.flab.just_10_minutes.User.service.UserFacadeService;
import com.flab.just_10_minutes.User.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flab.just_10_minutes.User.UserTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserFacadeServiceTest {

    @InjectMocks
    private PointService pointService;
    @Mock
    private PointMapper pointMapper;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserFacadeService target;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userMapper);
        pointService = new PointService(userService, pointMapper);
        target = new UserFacadeService(userService, pointService);
    }

    @Test
    public void findByLoginId는_존재하지_않는_유저라면_예외를_반환한다() {
        //given
        //when
        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);

        //then
        assertThrows(RuntimeException.class, () -> target.findByLoginId(NOT_EXIST_ID));
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유저가 보유한 포인트 조회 테스트")
    class UserPointTest {

        @Test
        @DisplayName("보유한 포인트 기록이 없는 경우 포인트 기록의 사이즈는 0 이다")
        public void whenUserHaveEmptyPoint() {
            //given
            User existUser = createUser();

            //when
            doReturn(existUser).when(userMapper).findByLoginId(EXIST_ID);
            doReturn(null).when(pointMapper).findByLoginId(EXIST_ID);

            UserInfoDto existUserInfo = target.findByLoginId(EXIST_ID);

            //then
            assertThat(existUserInfo.getPoint().getHistories().size()).isEqualTo(0);
        }
    }


}
