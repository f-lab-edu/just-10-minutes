package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class UserDtoTest {

    public static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static UserDto createTestUserDto(String loginId, String password, String phone, String address) {
        return UserDto.builder()
                .loginId(loginId)
                .password(password)
                .phone(phone)
                .address(address)
                .build();
    }

    public static final String NOT_EXIST_ID = "notExistId";
    public static final String VALID_LOGIN_ID = "testId";
    public static final String VALID_PASSWORD = "testPassword";
    public static final String VALID_PHONE = "010-1234-5678";
    public static final String VALID_ADDRESS = "testAddress";

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 필드 테스트 - null")
    class ParameterNotNullTest {

        @DisplayName("UserDto의 파라미터에 null이 들어오면 에러를 반환한다")
        @ParameterizedTest(name = "{4} : null 체크")
        @MethodSource("invalidUserDtoParameter")
        void invalidCreate(String loginId, String password, String phone, String address, String message) {
            Set<ConstraintViolation<UserDto>> validations = validator.validate(createTestUserDto(loginId, password, phone, address));
            assertThat(validations.size()).isEqualTo(1);
        }

        Stream<Arguments> invalidUserDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of(null, VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "loginId"),
                    Arguments.of(VALID_LOGIN_ID, null, VALID_PHONE, VALID_ADDRESS, "password"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, null, VALID_ADDRESS, "phone"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, VALID_PHONE, null, "email")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 필드 테스트 - empty")
    class ParameterNotEmptyTest {

        @DisplayName("UserDto의 파라미터에 \"\"이 들어오면 에러를 반환한다")
        @ParameterizedTest(name = "{4}")
        @MethodSource("invalidUserDtoParameterCount")
        void invalidCreate(String loginId, String password, String phone, String address, String message) {
            Set<ConstraintViolation<UserDto>> validations = validator.validate(createTestUserDto(loginId, password, phone, address));
            assertThat(validations.size()).isEqualTo(1);
        }

        @DisplayName("UserDto의 파라미터에 \"\"이 들어오면 에러를 반환한다")
        @ParameterizedTest(name = "{4}")
        @MethodSource("invalidUserDtoParameterCountWithSize")
        void invalidCreate2(String loginId, String password, String phone, String address, String message) {
            Set<ConstraintViolation<UserDto>> validations = validator.validate(createTestUserDto(loginId, password, phone, address));
            assertThat(validations.size()).isEqualTo(2);
        }

        Stream<Arguments> invalidUserDtoParameterCount() throws Throwable {
            return Stream.of(
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "", VALID_ADDRESS, "phone"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, VALID_PHONE, "", "email")
            );
        }

        Stream<Arguments> invalidUserDtoParameterCountWithSize() throws Throwable {
            return Stream.of(
                    Arguments.of("", VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "loginId"),
                    Arguments.of(VALID_LOGIN_ID, "", VALID_PHONE, VALID_ADDRESS, "password")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 필드 테스트 - null, empty 외")
    class ParameterSizeTest {

        @DisplayName("loginId는 5자리 미만 12자리 초과일 경우 예외를 반환한다")
        @ParameterizedTest(name = "{4} 자리 일 경우")
        @MethodSource("invalidLoginIdParameters")
        void invalidLoginIdCreate(String loginId, String password, String phone, String address, Integer loginIdLength) {
            Set<ConstraintViolation<UserDto>> validations = validator.validate(createTestUserDto(loginId, password, phone, address));
            assertThat(validations.size()).isEqualTo(1);
        }

        @DisplayName("password는 8자리 미만 15자리 초과일 경우 예외를 반환한다")
        @ParameterizedTest(name = "{4} 자리 일 경우")
        @MethodSource("invalidPasswordParameters")
        void invalidPasswordCreate(String loginId, String password, String phone, String address, Integer passwordLength) {
            Set<ConstraintViolation<UserDto>> validations = validator.validate(createTestUserDto(loginId, password, phone, address));
            assertThat(validations.size()).isEqualTo(1);
        }

        @DisplayName("phone은 010-(숫자 3-4자리)-(숫자 4자리) 가 아닐 경우 예외를 반환한다")
        @ParameterizedTest(name = "{4}")
        @MethodSource("invalidPhoneParameters")
        void invalidPhoneCreate(String loginId, String password, String phone, String address, String message) {
            Set<ConstraintViolation<UserDto>> validations = validator.validate(createTestUserDto(loginId, password, phone, address));
            assertThat(validations.size()).isEqualTo(1);
        }

        Stream<Arguments> invalidLoginIdParameters() throws Throwable {
            return Stream.of(
                    Arguments.of("abcd", VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "abcd".length()),
                    Arguments.of("abcdefghijklm", VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "abcdefghijklm".length())
            );
        }

        Stream<Arguments> invalidPasswordParameters() throws Throwable {
            return Stream.of(
                    Arguments.of(VALID_LOGIN_ID, "abcdefg", VALID_PHONE, VALID_ADDRESS, "abcdefg".length()),
                    Arguments.of(VALID_LOGIN_ID, "abcdefghijklmnop", VALID_PHONE, VALID_ADDRESS, "abcdefghijklmnop".length())
            );
        }

        Stream<Arguments> invalidPhoneParameters() throws Throwable {
            return Stream.of(
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "011-1234-5678", VALID_ADDRESS, "첫번째 자리가 010이 아닐 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010/1234-5678", VALID_ADDRESS, "첫번째 대시가 - 이 아닐 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-12-5678", VALID_ADDRESS, "두번째 자리가 3자리 미만일 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-12345-5678", VALID_ADDRESS, "두번째 자리가 4자리 초과일 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-123a-5678", VALID_ADDRESS, "두번째 자리에 숫자가 아닌 값이 있을 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-1234/5678", VALID_ADDRESS, "두번째 대시가 - 이 아닐 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-1234-567", VALID_ADDRESS, "세번쨰 자리에 4자리 미만일 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-1234-56781", VALID_ADDRESS, "세번쨰 자리에 4자리 초과일 때"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "010-1234-567a", VALID_ADDRESS, "세번쨰 자리에 숫자가 아닌 값이 있을 때")
            );
        }
    }

    @Test
    @DisplayName("UserDto 정상일 때")
    public void UserDto_정상_체크() {
        //given
        UserDto userDto = createTestUserDto("testId", "testPassword", "010-1234-5678", "testAddress");

        //when
        Set<ConstraintViolation<UserDto>> validations = validator.validate(userDto);

        //then
        assertThat(validations.size()).isEqualTo(0);
    }
}