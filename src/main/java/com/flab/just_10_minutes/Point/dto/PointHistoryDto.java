package com.flab.just_10_minutes.Point.dto;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PointHistoryDto {

    @NotEmpty
    private String loginId;
    @NotNull
    private Long requestQuantity;
    @NotEmpty
    private String reason;

    public static PointHistory toDomain(PointHistoryDto pointDto) {
        return PointHistory.builder()
                .loginId(pointDto.getLoginId())
                .requestedQuantity(pointDto.getRequestQuantity())
                .reason(pointDto.getReason())
                .totalQuantity(0L)
                .build();
    }
}
