package com.flab.just_10_minutes.user.fixture;

import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;

public class UserTestFixture {

    public static User createUser() {
        return User.builder()
                .id(1L)
                .loginId("testId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .point(0L)
                .build();
    }

    public static User createUser(String loginId) {
        return User.builder()
                .loginId(loginId)
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .point(0L)
                .build();
    }

    public static User createUser(String loginId, Long points) {
        return User.builder()
                .loginId(loginId)
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.PUBLIC)
                .point(points)
                .build();
    }

    public static User createSeller() {
        return User.builder()
                .loginId("sellerId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.SELLER)
                .build();
    }

    public static UserEntity createSellerEntity() {
        return UserEntity.builder()
                .loginId("sellerId")
                .password("testPassword")
                .phone("010-1234-5678")
                .address("testAddress")
                .role(User.ROLE.SELLER)
                .point(0L)
                .build();
    }
}
