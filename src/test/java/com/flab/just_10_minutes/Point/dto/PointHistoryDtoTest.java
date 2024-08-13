package com.flab.just_10_minutes.Point.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PointHistoryDtoTest {

    public static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static PointHistoryDto createTestPointHistoryDto(String loginId, Long quantity, String reason) {
        return PointHistoryDto.builder()
                            .loginId(loginId)
                            .quantity(quantity)
                            .reason(reason)
                            .build();
    }

    private static final String TEST_VALID_LOGIN_ID = "testId";
    private static final String TEST_REASON = "test";

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 필드 테스트 - null")
    class ParameterNotNullTest {

        @DisplayName("PointHistoryDto의 파라미터에 null이 들어오면 예외를 반환한다")
        @ParameterizedTest(name = "{3} : null 체크")
        @MethodSource("invalidPointHistoryDtoParameter")
        void invalidCreate(String loginId, Long quantity, String reason, String message)     {
            Set<ConstraintViolation<PointHistoryDto>> validations = validator.validate(createTestPointHistoryDto(loginId, quantity, reason));
            assertThat(validations.size()).isEqualTo(1);
        }

        Stream<Arguments> invalidPointHistoryDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of(null, 100L, TEST_REASON,  "loginId"),
                    Arguments.of(TEST_VALID_LOGIN_ID, null, TEST_REASON,  "quantity"),
                    Arguments.of(TEST_VALID_LOGIN_ID, 100L, null,  "loginId")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 필드 테스트 - empty")
    class ParameterNotEmptyTest {

        @DisplayName("PointHistoryDto의 파라미터에 \"\"이 들어오면 예외를 반환한다")
        @ParameterizedTest(name = "{3} : emtpy 체크")
        @MethodSource("invalidPointHistoryDtoParameter")
        void invalidCreate(String loginId, Long quantity, String reason, String message)     {
            Set<ConstraintViolation<PointHistoryDto>> validations = validator.validate(createTestPointHistoryDto(loginId, quantity, reason));
            assertThat(validations.size()).isEqualTo(1);
        }

        Stream<Arguments> invalidPointHistoryDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of("", 100L, TEST_REASON,  "loginId"), Arguments.of(TEST_VALID_LOGIN_ID, null, TEST_REASON,  "quantity"),
                    Arguments.of(TEST_VALID_LOGIN_ID, 100L, "",  "loginId")
            );
        }
    }
}
