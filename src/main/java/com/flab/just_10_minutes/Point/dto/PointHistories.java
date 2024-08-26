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
public class PointHistories {

    private Long totalQuantity;
    private List<PointHistoryResponse> histories;

    public static PointHistories from(Long totalQuantity, List<PointHistoryResponse> dtos) {
        return PointHistories.builder()
                .totalQuantity(totalQuantity)
                .histories(dtos)
                .build();
    }
}
