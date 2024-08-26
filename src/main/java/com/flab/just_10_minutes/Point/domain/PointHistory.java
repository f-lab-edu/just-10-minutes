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

    public PointHistory decrease(final Long userOwnPoint) {
        if (Math.abs(this.requestQuantity) > userOwnPoint) {
            return PointHistory.builder()
                    .loginId(this.loginId)
                    .requestQuantity(-userOwnPoint)
                    .reason(this.reason)
                    .totalQuantity(0L)
                    .build();
        }
        return PointHistory.builder()
                .loginId(this.loginId)
                .requestQuantity(this.requestQuantity)
                .reason(this.reason)
                .totalQuantity(userOwnPoint - Math.abs(this.requestQuantity))
                .build();
    }

    public PointHistory increase(final Long userOwnPoint) {
        return  PointHistory.builder()
                .loginId(this.loginId)
                .requestQuantity(this.requestQuantity)
                .reason(this.reason)
                .totalQuantity(this.requestQuantity + userOwnPoint)
                .build();
    }

    public static PointHistory from(User user, final Long requestQuantity, String reason) {
        return PointHistory.builder()
                .loginId(user.getLoginId())
                .requestQuantity(requestQuantity)
                .reason(reason)
                .build();
    }
}
