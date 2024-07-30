package com.flab.just_10_minutes.Point.dto;

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
}
