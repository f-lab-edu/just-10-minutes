package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.controller.UserController;
import com.flab.just_10_minutes.User.service.UserService;
import com.flab.just_10_minutes.Util.Handler.GlobalExceptionHandler;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.stream.Stream;

import static com.flab.just_10_minutes.User.UserDtoTestFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerParameterTest {

    @InjectMocks
    private UserController target;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 파라미터 : null")
    class ParameterNotNullTest {

        @DisplayName("signUp 실패 UserDto의 ")
        @ParameterizedTest(name = "{4} 이 null")
        @MethodSource("invalidUserDtoParameter")
        void invalidCreate(String loginId, String password, String phone, String address, String message) throws Exception {
            //given
            final String url = "/users/sign_up";

            //when
            final ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(url)
                            .content(gson.toJson(createTestUserDto(loginId, password, phone, address)))
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions.andExpect(status().isBadRequest());
        }

        Stream<Arguments> invalidUserDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of(null, PASSWORD, PHONE, ADDRESS, "loginId"),
                    Arguments.of(EXIST_ID, null, PHONE, ADDRESS, "password"),
                    Arguments.of(EXIST_ID, PASSWORD, null, ADDRESS, "phone"),
                    Arguments.of(EXIST_ID, PASSWORD, PHONE, null, "email")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 파라미터 - empty")
    class ParameterNotEmptyTest {

        @DisplayName("signUp 실패 UserDto의 ")
        @ParameterizedTest(name = "{4}  이 empty")
        @MethodSource("invalidUserDtoParameter")
        void invalidCreate(String loginId, String password, String phone, String address, String message) throws Exception {
            //given
            final String url = "/users/sign_up";

            //when
            final ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(url)
                            .content(gson.toJson(createTestUserDto(loginId, password, phone, address)))
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions.andExpect(status().isBadRequest());
        }

        Stream<Arguments> invalidUserDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of("", PASSWORD, PHONE, ADDRESS, "loginId"),
                    Arguments.of(EXIST_ID, "", PHONE, ADDRESS, "password"),
                    Arguments.of(EXIST_ID, PASSWORD, "", ADDRESS, "phone"),
                    Arguments.of(EXIST_ID, PASSWORD, PHONE, "", "email")
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 파라미터 - 자리수")
    class ParameterSizeTest {

        @DisplayName("signUp 실패 " )
        @ParameterizedTest(name = "{4}의 길이가 {5}")
        @MethodSource("invalidUserDtoParameter")
        void invalidLoginIdCreate(String loginId, String password, String phone, String address, String field,Integer fieldLength) throws Exception {
            //given
            final String url = "/users/sign_up";

            //when
            final ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(url)
                            .content(gson.toJson(createTestUserDto(loginId, password, phone, address)))
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions.andExpect(status().isBadRequest());
        }

        Stream<Arguments> invalidUserDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of("abcd", PASSWORD, PHONE, ADDRESS, "loginId", "abcd".length()),
                    Arguments.of("abcdefghijklm", PASSWORD, PHONE, ADDRESS, "loginId", "abcdefghijklm".length()),
                    Arguments.of(EXIST_ID, "abcdefg", PHONE, ADDRESS, "password", "abcdefg".length()),
                    Arguments.of(EXIST_ID, "abcdefghijklmnop", PHONE, ADDRESS, "password", "abcdefghijklmnop".length())
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("유효하지 않은 파라미터 - 전화번호")
    class ParameterPhoneNumTest {

        @DisplayName("signUp 실패 " )
        @ParameterizedTest(name = "{4}")
        @MethodSource("invalidUserDtoParameter")
        void invalidLoginIdCreate(String loginId, String password, String phone, String address, String message) throws Exception {
            //given
            final String url = "/users/sign_up";

            //when
            final ResultActions resultActions = mockMvc.perform(
                    MockMvcRequestBuilders.post(url)
                            .content(gson.toJson(createTestUserDto(loginId, password, phone, address)))
                            .contentType(MediaType.APPLICATION_JSON)
            );

            //then
            resultActions.andExpect(status().isBadRequest());
        }

        Stream<Arguments> invalidUserDtoParameter() throws Throwable {
            return Stream.of(
                    Arguments.of(EXIST_ID, PASSWORD, "011-1234-5678", ADDRESS, "첫번째 자리가 010이 아닐 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010/1234-5678", ADDRESS, "첫번째 대시가 - 이 아닐 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-12-5678", ADDRESS, "두번째 자리가 3자리 미만일 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-12345-5678", ADDRESS, "두번째 자리가 4자리 초과일 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-123a-5678", ADDRESS, "두번째 자리에 숫자가 아닌 값이 있을 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-1234/5678", ADDRESS, "두번째 대시가 - 이 아닐 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-1234-567", ADDRESS, "세번쨰 자리에 4자리 미만일 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-1234-56781", ADDRESS, "세번쨰 자리에 4자리 초과일 때"),
                    Arguments.of(EXIST_ID, PASSWORD, "010-1234-567a", ADDRESS, "세번쨰 자리에 숫자가 아닌 값이 있을 때")
            );
        }
    }

    @Test
    public void signUp_실패_파라미터가_null() throws Exception {
        //given
        final String url = "/users/sign_up";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(null))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isBadRequest());
    }
}
