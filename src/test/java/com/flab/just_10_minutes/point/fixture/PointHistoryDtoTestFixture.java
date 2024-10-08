package com.flab.just_10_minutes.point.fixture;

import com.flab.just_10_minutes.point.dto.PointHistoryDto;

public class PointHistoryDtoTestFixture {

    public static PointHistoryDto createTestPointHistoryDto(String loginId, Long quantity, String reason) {
        return PointHistoryDto.builder()
                .loginId(loginId)
                .requestQuantity(quantity)
                .reason(reason)
                .build();
    }

    public static final String NOT_EXIST_ID = "notExistId";
    public static final String EXIST_ID = "existId";
    public static final String REASON = "testReason";
}