package com.flab.just_10_minutes.Point;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointStatusDto;
import com.flab.just_10_minutes.Point.infrastructure.PointDao;
import com.flab.just_10_minutes.Point.service.PointService;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.Util.Exception.Database.DatabaseException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Optional;

import static com.flab.just_10_minutes.Point.PointHistoryTestFixture.createPointHistory;
import static com.flab.just_10_minutes.User.UserDtoTestFixture.EXIST_ID;
import static com.flab.just_10_minutes.User.UserDtoTestFixture.NOT_EXIST_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService target;
    @Mock
    private PointDao pointDao;
    @Mock
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        target = new PointService(userDao, pointDao);
    }

    @Test
    public void offerPoint_실패_존재하지_않는_회원() {
        //given
        PointHistory plusHistory = createPointHistory(NOT_EXIST_ID, 100L);

        doThrow(NotFoundException.class).when(userDao).fetch(NOT_EXIST_ID);

        //when
        final DatabaseException result = assertThrows(DatabaseException.class, () -> target.offerPoint(plusHistory));

        //then
        assertThat(result instanceof NotFoundException).isEqualTo(true);
    }

    @Test
    public void offerPoint_포인트_추가_성공_포인트_기록_최초_저장() {
        //given
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L);
        PointHistory newHistory = initHistory.calculateTotalQuantity(null);

        doReturn(null).when(userDao).fetch(EXIST_ID);
        doReturn(Optional.of(newHistory)).when(pointDao).calculateTotalQuantity(initHistory);
        doNothing().when(pointDao).save(any(PointHistory.class));
        doReturn(newHistory).when(pointDao).findTopByOrderByLoginIdDesc(EXIST_ID);

        //when
        PointHistory saveHistory = target.offerPoint(initHistory);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        verify(pointDao, times(1)).calculateTotalQuantity(any(PointHistory.class));
        verify(pointDao, times(1)).save(any(PointHistory.class));
        verify(pointDao, times(1)).findTopByOrderByLoginIdDesc(EXIST_ID);
        assertThat(newHistory).isEqualTo(saveHistory);
    }

    @Test
    public void offerPoint_포인트_추가_성공_포인트_기록_최초_저장2() {
        //given
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L).calculateTotalQuantity(null);
        PointHistory newHistory = createPointHistory(EXIST_ID, 200L);
        PointHistory latestHistory = newHistory.calculateTotalQuantity(initHistory);


        doReturn(null).when(userDao).fetch(EXIST_ID);
        doReturn(Optional.of(latestHistory)).when(pointDao).calculateTotalQuantity(newHistory);
        doNothing().when(pointDao).save(any(PointHistory.class));
        doReturn(latestHistory).when(pointDao).findTopByOrderByLoginIdDesc(EXIST_ID);

        //when
        PointHistory saveHistory = target.offerPoint(newHistory);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        verify(pointDao, times(1)).calculateTotalQuantity(any(PointHistory.class));
        verify(pointDao, times(1)).save(any(PointHistory.class));
        verify(pointDao, times(1)).findTopByOrderByLoginIdDesc(EXIST_ID);
        assertThat(latestHistory).isEqualTo(saveHistory);
    }

    @Test
    public void getTotalPoint_최초_기록이_없는_경우() {
        //given
        doReturn(null).when(userDao).fetch(EXIST_ID);
        doReturn(null).when(pointDao).findTopByOrderByLoginIdDesc(EXIST_ID);

        //when
        Long totalPoint = target.getTotalPoint(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        verify(pointDao, times(1)).findTopByOrderByLoginIdDesc(EXIST_ID);
        assertThat(totalPoint).isEqualTo(0L);
    }

    @Test
    public void getTotalPoint_기록이_있는_경우() {
        //given
        PointHistory latestHistory = createPointHistory(EXIST_ID, 200L, "test", 1000L);

        doReturn(null).when(userDao).fetch(EXIST_ID);
        doReturn(latestHistory).when(pointDao).findTopByOrderByLoginIdDesc(EXIST_ID);

        //when
        Long totalPoint = target.getTotalPoint(EXIST_ID);

        //then
        verify(userDao, times(1)).fetch(EXIST_ID);
        verify(pointDao, times(1)).findTopByOrderByLoginIdDesc(EXIST_ID);
        assertThat(totalPoint).isEqualTo(1000L);
    }

    @Test
    public void getPointHistories_최초_기록이_없는_경우() {
        //given
        doReturn(null).when(pointDao).findTopByOrderByLoginIdDesc(EXIST_ID);
        doReturn(new ArrayList<>()).when(pointDao).findByLoginId(EXIST_ID);

        //when
        PointStatusDto pointHistories = target.getPointHistories(EXIST_ID);

        //then
        verify(pointDao, times(1)).findTopByOrderByLoginIdDesc(EXIST_ID);
        verify(pointDao, times(1)).findByLoginId(EXIST_ID);
        assertThat(pointHistories.getTotalQuantity()).isEqualTo(0L);
        assertThat(pointHistories.getHistories().size()).isEqualTo(0);
    }

    @Test
    public void getPointHistories_기록이_있는_경우() {
        //setUp
        ArrayList<PointHistory> histories = new ArrayList<>();
        PointHistory initHistory = createPointHistory(EXIST_ID, 100L).calculateTotalQuantity(null);
        PointHistory secondHistory = createPointHistory(EXIST_ID, 200L).calculateTotalQuantity(initHistory);
        histories.add(initHistory);
        histories.add(secondHistory);

        //given
        doReturn(secondHistory).when(pointDao).findTopByOrderByLoginIdDesc(EXIST_ID);
        doReturn(histories).when(pointDao).findByLoginId(EXIST_ID);

        //when
        PointStatusDto pointHistories = target.getPointHistories(EXIST_ID);

        //then
        verify(pointDao, times(1)).findTopByOrderByLoginIdDesc(EXIST_ID);
        verify(pointDao, times(1)).findByLoginId(EXIST_ID);
        assertThat(pointHistories.getTotalQuantity()).isEqualTo(300L);
        assertThat(pointHistories.getHistories().size()).isEqualTo(2);
    }
}