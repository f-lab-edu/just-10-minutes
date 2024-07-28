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

    public PointHistory calculateTotalQuantity(PointHistory latestHistory) {
        if (latestHistory == null) {
            if (this.quantity <= 0) {
                return null;
            } else {
                return PointHistory.builder()
                        .loginId(this.loginId)
                        .quantity(this.quantity)
                        .reason(this.reason)
                        .totalQuantity(this.quantity)
                        .build();
            }
        } else {
            if (this.quantity <= 0) {
                if (Math.abs(this.quantity) > latestHistory.getTotalQuantity()) {
                    return PointHistory.builder()
                            .loginId(this.loginId)
                            .quantity(-latestHistory.getTotalQuantity())
                            .reason(this.reason)
                            .totalQuantity(0L)
                            .build();
                } else {
                    return PointHistory.builder()
                            .loginId(this.loginId)
                            .quantity(this.quantity)
                            .reason(this.reason)
                            .totalQuantity(latestHistory.getTotalQuantity() - Math.abs(this.quantity))
                            .build();
                }
            } else {
                return  PointHistory.builder()
                        .loginId(this.loginId)
                        .quantity(this.quantity)
                        .reason(this.reason)
                        .totalQuantity(this.quantity + latestHistory.getTotalQuantity())
                        .build();
            }
        }
    }
}
