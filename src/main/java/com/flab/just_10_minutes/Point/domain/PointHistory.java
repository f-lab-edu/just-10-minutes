package com.flab.just_10_minutes.Point.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class PointHistory {

    private String loginId;
    private Long quantity;
    private String reason;
    private Long totalQuantity;

    public PointHistory decrease(final Long userOwnPoints) {
        if (Math.abs(this.quantity) > userOwnPoints) {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .quantity(-userOwnPoints)
                    .reason(this.reason)
                    .totalQuantity(0L)
                    .build();
        } else {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .quantity(this.quantity)
                    .reason(this.reason)
                    .totalQuantity(userOwnPoints - Math.abs(this.quantity))
                    .build();
        }
    }

    public PointHistory increase(final Long userOwnPoints) {
        return  PointHistory.builder()
                .loginId(this.loginId)
                .quantity(this.quantity)
                .reason(this.reason)
                .totalQuantity(this.quantity + userOwnPoints)
                .build();
    }
}
