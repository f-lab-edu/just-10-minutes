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

    public PointHistory decrease(final Long requestDecrease) {
        if (Math.abs(this.requestQuantity) > requestDecrease) {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .requestQuantity(-requestDecrease)
                    .reason(this.reason)
                    .totalQuantity(0L)
                    .build();
        }
        return PointHistory.builder()
                .loginId(this.loginId)
                .requestQuantity(this.requestQuantity)
                .reason(this.reason)
                .totalQuantity(requestDecrease - Math.abs(this.requestQuantity))
                .build();
    }

    public PointHistory increase(final Long requestIncrease) {
        return  PointHistory.builder()
                .loginId(this.loginId)
                .requestQuantity(this.requestQuantity)
                .reason(this.reason)
                .totalQuantity(this.requestQuantity + requestIncrease)
                .build();
    }

    public boolean isRequestZero() {
        return this.requestQuantity == 0L;
    }

    public static PointHistory from(User user, final Long requestQuantity, String reason) {
        return PointHistory.builder()
                .loginId(user.getLoginId())
                .requestQuantity(requestQuantity)
                .reason(reason)
                .build();
    }
}
