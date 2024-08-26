package com.flab.just_10_minutes.Point.dto;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryResponseDto {

    private Long requestQuantity;
    private String reason;

    public static PointHistoryResponseDto from(PointHistory pointHistory) {
        return PointHistoryResponseDto.builder()
                                    .requestQuantity(pointHistory.getRequestQuantity())
                                    .reason(pointHistory.getReason())
                                    .build();
    }
}
