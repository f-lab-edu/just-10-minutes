package com.flab.just_10_minutes.point.fixture;

import com.flab.just_10_minutes.point.domain.PointHistory;

import static com.flab.just_10_minutes.user.fixture.UserDtoTestFixture.EXIST_ID;

public class PointHistoryTestFixture {

    public static PointHistory createPointHistory(Long quantity) {
        return PointHistory.builder()
                .loginId(EXIST_ID)
                .requestedQuantity(quantity)
                .reason("test")
                .totalQuantity(0L)
                .build();
    }

    public static PointHistory createPointHistory(String loginId, Long quantity) {
        return PointHistory.builder()
                .loginId(loginId)
                .requestedQuantity(quantity)
                .reason("test")
                .totalQuantity(0L)
                .build();
    }

    public static PointHistory createPointHistory(String loginId, Long quantity, String reason) {
        return PointHistory.builder()
                .loginId(loginId)
                .requestedQuantity(quantity)
                .reason(reason)
                .totalQuantity(0L)
                .build();
    }

    public static PointHistory createPointHistory(String loginId, Long quantity, String reason, Long totalPoint) {
        return PointHistory.builder()
                .loginId(loginId)
                .requestedQuantity(quantity)
                .reason(reason)
                .totalQuantity(totalPoint)
                .build();
    }
}
