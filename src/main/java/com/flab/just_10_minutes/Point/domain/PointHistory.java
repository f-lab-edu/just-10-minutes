package com.flab.just_10_minutes.Point.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PointHistory {

    private Long id;

    private String loginId;

    private Long quantity;

    private String reason;
}
