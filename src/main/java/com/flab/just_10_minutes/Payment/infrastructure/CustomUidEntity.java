package com.flab.just_10_minutes.Payment.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CustomUidEntity {

    private String loginId;
    private String customUid;

    public static CustomUidEntity from(final String loginId, final String customUid) {
        return CustomUidEntity.builder()
                                .loginId(loginId)
                                .customUid(customUid)
                                .build();
    }
}
