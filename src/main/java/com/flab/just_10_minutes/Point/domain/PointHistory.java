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
    private Long requestQuantity;
    private String reason;
    private Long totalQuantity;

    public PointHistory decrease(final Long requestedDecrease) {
        if (Math.abs(this.requestQuantity) > requestedDecrease) {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .requestQuantity(-requestedDecrease)
                    .reason(this.reason)
                    .totalQuantity(0L)
                    .build();
        }
        return PointHistory.builder()
                .loginId(this.loginId)
                .requestQuantity(this.requestQuantity)
                .reason(this.reason)
                .totalQuantity(requestedDecrease - Math.abs(this.requestQuantity))
                .build();
    }

    public PointHistory increase(final Long requestedIncrease) {
        return  PointHistory.builder()
                .loginId(this.loginId)
                .requestQuantity(this.requestQuantity)
                .reason(this.reason)
                .totalQuantity(this.requestQuantity + requestedIncrease)
                .build();
    }

    public boolean isRequestZero() {
        return this.requestQuantity == 0L;
    }

    public static PointHistory from(User user, final Long requestedQuantity, String reason) {
        return PointHistory.builder()
                .loginId(user.getLoginId())
                .requestQuantity(requestedQuantity)
                .reason(reason)
                .build();
    }
}
