package com.flab.just_10_minutes.user.fixture;

import com.flab.just_10_minutes.user.dto.UserDto;

public class UserDtoTestFixture {

    public static UserDto createTestUserDto(String loginId, String password, String phone, String address) {
        return UserDto.builder()
                .loginId(loginId)
                .password(password)
                .phone(phone)
                .address(address)
                .build();
    }

    public static final String NOT_EXIST_ID = "notExistId";
    public static final String EXIST_ID = "existId";
    public static final String PASSWORD = "testPassword";
    public static final String PHONE = "010-1234-5678";
    public static final String ADDRESS = "testAddress";
}