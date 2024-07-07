package com.flab.just_10_minutes.User.domain;

import com.flab.just_10_minutes.User.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class UserTest {

    public static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static UserDto createTestUser(String loginId, String password, String phone, String address) {
        return UserDto.builder()
                .loginId(loginId)
                .password(password)
                .phone(phone)
                .address(address)
                .build();
    }

    @Test
    public void UserDto_loginId_NotNull_체크() {
        //given
        UserDto userDto = createTestUser(null, "testPassword", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_loginId_NotEmpty_체크() {
        //given
        UserDto userDto = createTestUser("", "testPassword", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(2);
    }

    @Test
    public void UserDto_loginId_5자리_미만_체크() {
        //given
        UserDto userDto = createTestUser("abcd", "testPassword", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_loginId_12자리_초과_체크() {
        //given
        UserDto userDto = createTestUser("abcdefghijklm", "testPassword", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_password_NotNull_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", null, "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_password_NotEmpty_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(2);
    }

    @Test
    public void UserDto_password_8자리_미만_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "abcdefg", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_password_15자리_초과_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "abcdefghijklmnop", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_NotNull_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", null, "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_NotEmpty_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_첫번째_자리가_010이_아닐때_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "011-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_첫번째_대시_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010/1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_두번째_자리수_3자리_미만_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-12-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_두번째_자리수_4자리_초과_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-12345-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_두번째_자리_숫자가_아닌값_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-123a-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_두번째_대시_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234/5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_세번째_자리_4자리_미만_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234-567", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_세번째_자리_4자리_초과_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234-56781", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_phone_세번째_자리_숫자가_아닌값_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234-567a", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }
    @Test
    public void UserDto_address_NotNull_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234-5678", null);

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_address_NotEmpty_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234-5678", "");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(1);
    }

    @Test
    public void UserDto_정상_체크() {
        //given
        UserDto userDto = createTestUser("testLoginId", "testPassword", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(0);
    }
}