package com.flab.just_10_minutes.Point.infrastructure.entity;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PointHistoryEntity {

    private String loginId;
    private Long requestQuantity;
    private String reason;
    private Long totalQuantity;

    public static PointHistoryEntity from(PointHistory pointHistory) {
        return PointHistoryEntity.builder()
                                .loginId(pointHistory.getLoginId())
                                .requestQuantity(pointHistory.getRequestQuantity())
                                .reason(pointHistory.getReason())
                                .totalQuantity(pointHistory.getTotalQuantity())
                                .build();
    }

    public static PointHistory to(PointHistoryEntity pointHistoryEntity) {
        return PointHistory.builder()
                .loginId(pointHistoryEntity.getLoginId())
                .requestQuantity(pointHistoryEntity.getRequestQuantity())
                .reason(pointHistoryEntity.getReason())
                .totalQuantity(pointHistoryEntity.getTotalQuantity())
                .build();
    }
}
