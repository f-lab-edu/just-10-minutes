package com.flab.just_10_minutes.Point.domain;

import com.flab.just_10_minutes.User.domain.User;
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

    public PointHistory decrease(final Long userOwnPoint) {
        if (Math.abs(this.quantity) > userOwnPoint) {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .quantity(-userOwnPoint)
                    .reason(this.reason)
                    .totalQuantity(0L)
                    .build();
        }
        return PointHistory.builder()
                .loginId(this.loginId)
                .quantity(this.quantity)
                .reason(this.reason)
                .totalQuantity(userOwnPoint - Math.abs(this.quantity))
                .build();
    }

    public PointHistory increase(final Long userOwnPoint) {
        return  PointHistory.builder()
                .loginId(this.loginId)
                .quantity(this.quantity)
                .reason(this.reason)
                .totalQuantity(this.quantity + userOwnPoint)
                .build();
    }

    public static PointHistory from(User user, final Long requestDecreaseQuantity, String reason) {
        return PointHistory.builder()
                .loginId(user.getLoginId())
                .quantity(requestDecreaseQuantity)
                .reason(reason)
                .build();
    }
}
