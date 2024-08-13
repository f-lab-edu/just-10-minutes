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

    private Long quantity;
    private String reason;

    public static PointHistoryResponseDto from(PointHistory pointHistory) {
        return PointHistoryResponseDto.builder()
                                    .quantity(pointHistory.getQuantity())
                                    .reason(pointHistory.getReason())
                                    .build();
    }
}
