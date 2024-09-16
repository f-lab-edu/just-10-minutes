package com.flab.just_10_minutes.point.dto;

import com.flab.just_10_minutes.point.domain.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryResponse {

    private Long requestQuantity;
    private String reason;

    public static PointHistoryResponse from(PointHistory pointHistory) {
        return PointHistoryResponse.builder()
                                    .requestQuantity(pointHistory.getRequestedQuantity())
                                    .reason(pointHistory.getReason())
                                    .build();
    }
}
