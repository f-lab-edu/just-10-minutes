package com.flab.just_10_minutes.Point;

import com.flab.just_10_minutes.Point.domain.PointHistory;

import static com.flab.just_10_minutes.User.UserTestFixture.EXIST_ID;

public class PointHistoriesTestFixture {

    public static PointHistory createPointHistory(Long quantity) {
        return PointHistory.builder()
                .loginId(EXIST_ID)
                .quantity(quantity)
                .reason("test")
                .build();
    }

    public static PointHistory createPointHistory(String loginId, Long quantity) {
        return PointHistory.builder()
                .loginId(loginId)
                .quantity(quantity)
                .reason("test")
                .build();
    }
}
