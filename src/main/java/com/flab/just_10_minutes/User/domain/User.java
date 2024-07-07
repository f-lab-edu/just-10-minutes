package com.flab.just_10_minutes.User.domain;

public class User {
    private Long id;
    private String password;
    private String phone;
    private String address;
    private ROLE role;
    //TODO : point 추가

    enum ROLE {
        PUBLIC,
        SELLER,
        ADMIN
    }
}
