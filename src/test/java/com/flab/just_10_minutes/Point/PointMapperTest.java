package com.flab.just_10_minutes.Point;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.mapper.PointMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import static com.flab.just_10_minutes.Point.PointHistoriesTestFixture.createPointHistory;
import static com.flab.just_10_minutes.User.UserTestFixture.EXIST_ID;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@MybatisTest
public class PointMapperTest {

    @Autowired
    private PointMapper pointMapper;

    @Test
    public void save_성공_포인트_추가() {
        //given
        PointHistory plusPointHistory = createPointHistory(100L);

        //when
        pointMapper.save(plusPointHistory);
        List<PointHistory> histories = pointMapper.findByLoginId(EXIST_ID);

        //then
        assertThat(histories.size()).isEqualTo(1);
        assertThat(histories.get(0).getQuantity()).isEqualTo(100L);
    }

    @Test
    public void save_성공_포인트_차감() {
        //given
        PointHistory minusPointHistory = createPointHistory(-100L);

        //when
        pointMapper.save(minusPointHistory);
        List<PointHistory> histories = pointMapper.findByLoginId(EXIST_ID);

        //then
        assertThat(histories.size()).isEqualTo(1);
        assertThat(histories.get(0).getQuantity()).isEqualTo(-100L);
    }

    @Test
    public void findByUserId_포인트기록이_있을_때() {
        //given
        PointHistory plusPointHistory = createPointHistory(100L);
        PointHistory minusPointHistory = createPointHistory(-100L);

        //when
        pointMapper.save(plusPointHistory);
        pointMapper.save(minusPointHistory);
        List<PointHistory> histories = pointMapper.findByLoginId(EXIST_ID);

        //then
        assertThat(histories.size()).isEqualTo(2);
    }

    @Test
    public void findByUserId_포인트기록이_없을_때() {
        //given
        //when
        List<PointHistory> histories = pointMapper.findByLoginId(EXIST_ID);

        //then
        assertThat(histories.size()).isEqualTo(0);
    }
}
