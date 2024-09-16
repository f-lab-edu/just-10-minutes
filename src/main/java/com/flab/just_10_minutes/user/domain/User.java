package com.flab.just_10_minutes.user.domain;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class User {

    private Long id;
    private String loginId;
    private String password;
    private String phone;
    private String address;
    private ROLE role;
    private Long point;

    public enum ROLE {
        PUBLIC,
        SELLER,
        ADMIN
    }
}
