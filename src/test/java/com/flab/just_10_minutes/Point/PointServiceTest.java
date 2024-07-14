package com.flab.just_10_minutes.Point;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.mapper.PointMapper;
import com.flab.just_10_minutes.Point.service.PointService;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import com.flab.just_10_minutes.User.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static com.flab.just_10_minutes.Point.PointHistoriesTestFixture.createPointHistory;
import static com.flab.just_10_minutes.User.UserTestFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService target;
    @Mock
    private PointMapper pointMapper;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userMapper);
        target = new PointService(userService, pointMapper);
    }

    @Test
    public void save_실패_회원이_존재하지_않으면_예외를_반환한다() {
        //given
        PointHistory plusPointHistory = createPointHistory(NOT_EXIST_ID, 100L);

        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);

        //when
        //then
        assertThrows(RuntimeException.class, () -> target.save(plusPointHistory));
    }

    @Test
    public void save_성공_포인트_추가() {
        //given
        PointHistory plusPointHistory = createPointHistory(100L);
        User existUser = createUser();

        //when
        doReturn(existUser).when(userMapper).findByLoginId(EXIST_ID);
        doReturn(1).when(userMapper).update(EXIST_ID, 100L);
        doReturn(1).when(pointMapper).save(plusPointHistory);

        target.save(plusPointHistory);

        //then
        verify(userMapper, times(1)).findByLoginId(EXIST_ID);
        verify(userMapper, times(1)).update(EXIST_ID, 100L);
        verify(pointMapper, times(1)).save(any(PointHistory.class));
    }

    @Test
    public void save_성공_포인트_차감() {
        //given
        PointHistory minusPointHistory = createPointHistory(-100L);

        User existUser = createUser();

        //when
        doReturn(existUser).when(userMapper).findByLoginId(EXIST_ID);
        doReturn(1).when(userMapper).update(EXIST_ID, -100L);
        doReturn(1).when(pointMapper).save(minusPointHistory);

        target.save(minusPointHistory);

        //then
        verify(userMapper, times(1)).findByLoginId(EXIST_ID);
        verify(userMapper, times(1)).update(EXIST_ID, -100L);
        verify(pointMapper, times(1)).save(any(PointHistory.class));
    }

    @Test
    public void findByLoginId_실패_회원이_존재하지_않으면_예외를_반환한다() {
        //given
        doReturn(null).when(userMapper).findByLoginId(NOT_EXIST_ID);

        //when
        //then
        assertThrows(RuntimeException.class, () -> target.findByLoginId(NOT_EXIST_ID));
    }

    @Test
    public void findByLoginId_성공_포인트_이력이_존재할_경우() {
        //given
        PointHistory plusPointHistory = createPointHistory(100L);
        PointHistory minusPointHistory = createPointHistory(-100L);

        List<PointHistory> histories = new ArrayList<>();
        histories.add(plusPointHistory);
        histories.add(minusPointHistory);

        User existUser = createUser();

        //when
        doReturn(existUser).when(userMapper).findByLoginId(EXIST_ID);
        doReturn(histories).when(pointMapper).findByLoginId(EXIST_ID);
        List<PointHistory> userPointHistories = target.findByLoginId(EXIST_ID);

        //then
        verify(userMapper, times(1)).findByLoginId(EXIST_ID);
        verify(pointMapper, times(1)).findByLoginId(EXIST_ID);
        Assertions.assertThat(userPointHistories.size()).isEqualTo(2);
    }

    @Test
    public void findByLoginId_성공_포인트_이력이_없을_때() {
        //given
        List<PointHistory> histories = new ArrayList<>();

        User existUser = createUser();

        //when
        doReturn(existUser).when(userMapper).findByLoginId(EXIST_ID);
        doReturn(histories).when(pointMapper).findByLoginId(EXIST_ID);
        List<PointHistory> userPointHistories = target.findByLoginId(EXIST_ID);


        //then
        verify(userMapper, times(1)).findByLoginId(EXIST_ID);
        verify(pointMapper, times(1)).findByLoginId(EXIST_ID);
        Assertions.assertThat(userPointHistories.size()).isEqualTo(0);
    }
}