package com.flab.just_10_minutes.Payment.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BillingKeyEntity {

    private String loginId;
    private String customerUid;

    public static BillingKeyEntity from(final String loginId, final String customUid) {
        return BillingKeyEntity.builder()
                                .loginId(loginId)
                                .customerUid(customUid)
                                .build();
    }
}
