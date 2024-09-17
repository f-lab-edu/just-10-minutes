package com.flab.just_10_minutes.point.infrastructure.entity;

import com.flab.just_10_minutes.point.domain.PointHistory;
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
    private Long requestedQuantity;
    private String reason;
    private Long totalQuantity;

    public static PointHistoryEntity from(PointHistory pointHistory) {
        return PointHistoryEntity.builder()
                                .loginId(pointHistory.getLoginId())
                                .requestedQuantity(pointHistory.getRequestedQuantity())
                                .reason(pointHistory.getReason())
                                .totalQuantity(pointHistory.getTotalQuantity())
                                .build();
    }

    public static PointHistory toDomain(PointHistoryEntity pointHistoryEntity) {
        return PointHistory.builder()
                .loginId(pointHistoryEntity.getLoginId())
                .requestedQuantity(pointHistoryEntity.getRequestedQuantity())
                .reason(pointHistoryEntity.getReason())
                .totalQuantity(pointHistoryEntity.getTotalQuantity())
                .build();
    }
}
