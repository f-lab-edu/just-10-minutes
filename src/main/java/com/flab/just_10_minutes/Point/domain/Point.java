package com.flab.just_10_minutes.Point.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class Point {

    private Long totalQuantity;

    private List<PointHistory> histories;
}
