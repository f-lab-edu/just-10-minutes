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
    private Long requestedQuantity;
    private String reason;
    private Long totalQuantity;

    public PointHistory decrease(final Long requestedDecrease) {
        if (Math.abs(this.requestedQuantity) > requestedDecrease) {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .requestedQuantity(-requestedDecrease)
                    .reason(this.reason)
                    .totalQuantity(0L)
                    .build();
        }
        return PointHistory.builder()
                .loginId(this.loginId)
                .requestedQuantity(this.requestedQuantity)
                .reason(this.reason)
                .totalQuantity(requestedDecrease - Math.abs(this.requestedQuantity))
                .build();
    }

    public PointHistory increase(final Long requestedIncrease) {
        return  PointHistory.builder()
                .loginId(this.loginId)
                .requestedQuantity(this.requestedQuantity)
                .reason(this.reason)
                .totalQuantity(this.requestedQuantity + requestedIncrease)
                .build();
    }

    public Boolean isRequestedZero() {
        return this.requestedQuantity == 0L;
    }

    public static PointHistory from(User user, final Long requestedQuantity, String reason) {
        return PointHistory.builder()
                .loginId(user.getLoginId())
                .requestedQuantity(requestedQuantity)
                .reason(reason)
                .build();
    }
}
