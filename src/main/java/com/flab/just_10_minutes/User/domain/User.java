package com.flab.just_10_minutes.User.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class User {

    private Long id;
    private String loginId;
    private String password;
    private String phone;
    private String address;
    private ROLE role;
    //TODO : point 추가

    public enum ROLE {
        PUBLIC,
        SELLER,
        ADMIN
    }
}
