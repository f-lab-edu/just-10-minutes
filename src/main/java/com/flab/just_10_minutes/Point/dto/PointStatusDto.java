package com.flab.just_10_minutes.Point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PointStatusDto {

    private Long totalQuantity;
    private List<PointHistoryResponseDto> histories;

    public static PointStatusDto from(Long totalQuantity, List<PointHistoryResponseDto> dtos) {
        return PointStatusDto.builder()
                .totalQuantity(totalQuantity)
                .histories(dtos)
                .build();
    }
}
