package com.flab.just_10_minutes.Point.service;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointHistories;
import com.flab.just_10_minutes.Point.infrastructure.repository.PointHistoryDao;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.repository.UserDao;
import com.flab.just_10_minutes.Util.Exception.Database.DatabaseException;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;

import static com.flab.just_10_minutes.Point.fixture.PointHistoryTestFixture.createPointHistory;
import static com.flab.just_10_minutes.User.fixture.UserDtoTestFixture.EXIST_ID;
import static com.flab.just_10_minutes.User.fixture.UserTestFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService target;
    @Mock
    private PointHistoryDao pointHistoryDao;
    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        target = new PointService(userDao, pointHistoryDao);
    }

    @Test
    public void offerPoint_실패_존재하지_않는_회원() {
        //given
        PointHistory plusHistory = createPointHistory(EXIST_ID, 100L);

        doThrow(NotFoundException.class).when(userDao).fetch(EXIST_ID);

        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.offerPoint(plusHistory));

        //then
        assertThat(result instanceof NotFoundException).isEqualTo(true);
    }


    @Test
    public void offerPoint_실패_히스토리_저장_실패() {
        //given
        User existUser = createUser(EXIST_ID, 1000L);
        PointHistory plusHistory = createPointHistory(EXIST_ID, 100L);

        doReturn(existUser).when(userDao).fetch(EXIST_ID);
        doThrow(InternalException.class).when(pointHistoryDao).save(any(PointHistory.class));

        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.offerPoint(plusHistory));

        //then
        assertThat(result instanceof InternalException).isEqualTo(true);
    }

    @Test
    public void offerPoint_실패_포인트_업데이트_실패() {
        //given
        User existUser = createUser(EXIST_ID, 1000L);
        PointHistory plusHistory = createPointHistory(EXIST_ID, 100L);
        PointHistory newHistory = plusHistory.increase(existUser.getPoint());

        doReturn(existUser).when(userDao).fetch(EXIST_ID);
        doNothing().when(pointHistoryDao).save(any(PointHistory.class));
        doThrow(InternalException.class).when(userDao).patchPoint(EXIST_ID, newHistory.getTotalQuantity());

        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.offerPoint(plusHistory));

        //then
        assertThat(result instanceof InternalException).isEqualTo(true);
    }

    @Test
    public void offerPoint_실패_기록이_없음() {
        //given
        User existUser = createUser(EXIST_ID, 1000L);
        PointHistory plusHistory = createPointHistory(EXIST_ID, 100L);
        PointHistory newHistory = plusHistory.increase(existUser.getPoint());

        doReturn(existUser).when(userDao).fetch(EXIST_ID);
        doNothing().when(pointHistoryDao).save(any(PointHistory.class));
        doNothing().when(userDao).patchPoint(EXIST_ID, newHistory.getTotalQuantity());
        doThrow(NotFoundException.class).when(pointHistoryDao).fetchFirst(EXIST_ID);

        //when
        final NotFoundException result = assertThrows(NotFoundException.class, () -> target.offerPoint(plusHistory));

        //then
        assertThat(result instanceof NotFoundException).isEqualTo(true);
    }

    @Test
    public void offerPoint_성공() {
        //given
        User existUser = createUser(EXIST_ID, 1000L);
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L);
        PointHistory newHistory = initHistory.increase(existUser.getPoint());

        doReturn(existUser).when(userDao).fetch(EXIST_ID);
        doNothing().when(pointHistoryDao).save(newHistory);
        doNothing().when(userDao).patchPoint(EXIST_ID, newHistory.getTotalQuantity());
        doReturn(newHistory).when(pointHistoryDao).fetchFirst(EXIST_ID);

        //when
        PointHistory saveHistory = target.offerPoint(initHistory);

        //then
        verify(pointHistoryDao, times(1)).save(any(PointHistory.class));
        verify(userDao, times(1)).patchPoint(EXIST_ID, newHistory.getTotalQuantity());
        verify(pointHistoryDao, times(1)).fetchFirst(EXIST_ID);
        assertThat(newHistory).isEqualTo(saveHistory);
    }

    @Test
    public void getTotalPoint_보유한_포인트가_없는_경우() {
        //given
        User existUser = createUser(EXIST_ID, 0L);
        doReturn(existUser).when(userDao).fetch(EXIST_ID);

        //when
        Long totalPoint = target.getTotalPoint(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        assertThat(totalPoint).isEqualTo(0L);
    }

    @Test
    public void getTotalPoint_보유한_포인트가_있는_경우() {
        //given
        User existUser = createUser(EXIST_ID, 1000L);
        doReturn(existUser).when(userDao).fetch(EXIST_ID);

        //when
        Long totalPoint = target.getTotalPoint(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        assertThat(totalPoint).isEqualTo(1000L);
    }

    @Test
    public void getPointHistories_기록이_없는_경우() {
        //given
        User existUser = createUser(EXIST_ID, 0L);

        doReturn(existUser).when(userDao).fetch(EXIST_ID);
        doReturn(new ArrayList<>()).when(pointHistoryDao).findByLoginId(EXIST_ID);

        //when
        PointHistories pointHistories = target.getPointHistories(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        verify(pointHistoryDao, times(1)).findByLoginId(EXIST_ID);
        assertThat(pointHistories.getTotalQuantity()).isEqualTo(0L);
        assertThat(pointHistories.getHistories().size()).isEqualTo(0);
    }

    @Test
    public void getPointHistories_기록이_있는_경우() {
        //setUp
        User existUser = createUser(EXIST_ID, 0L);

        ArrayList<PointHistory> histories = new ArrayList<>();
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L).increase(existUser.getPoint());
        existUser = createUser(EXIST_ID, initHistory.getTotalQuantity());

        PointHistory secondHistory = createPointHistory(EXIST_ID, 200L).increase(existUser.getPoint());
        existUser = createUser(EXIST_ID, secondHistory.getTotalQuantity());
        histories.add(initHistory);
        histories.add(secondHistory);

        //given
        doReturn(existUser).when(userDao).fetch(EXIST_ID);
        doReturn(histories).when(pointHistoryDao).findByLoginId(EXIST_ID);

        //when
        PointHistories pointHistories = target.getPointHistories(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        verify(pointHistoryDao, times(1)).findByLoginId(EXIST_ID);
        assertThat(pointHistories.getTotalQuantity()).isEqualTo(300L);
        assertThat(pointHistories.getHistories().size()).isEqualTo(2);
    }
}