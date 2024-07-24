package com.flab.just_10_minutes.User;

import com.flab.just_10_minutes.User.controller.UserController;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.service.UserService;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
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

import static com.flab.just_10_minutes.User.UserDtoTest.createTestUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController target;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private Gson gson;

    private static final String VALID_LOGIN_ID = "testId";
    private static final String VALID_PASSWORD = "testPassword";
    private static final String VALID_PHONE = "010-1234-5678";
    private static final String VALID_ADDRESS = "testAddress";

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
            final String url = "/users/sign-up";

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
                    Arguments.of(null, VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "loginId"),
                    Arguments.of(VALID_LOGIN_ID, null, VALID_PHONE, VALID_ADDRESS, "password"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, null, VALID_ADDRESS, "phone"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, VALID_PHONE, null, "email")
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
            final String url = "/users/sign-up";

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
                    Arguments.of("", VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "loginId"),
                    Arguments.of(VALID_LOGIN_ID, "", VALID_PHONE, VALID_ADDRESS, "password"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, "", VALID_ADDRESS, "phone"),
                    Arguments.of(VALID_LOGIN_ID, VALID_PASSWORD, VALID_PHONE, "", "email")
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
            final String url = "/users/sign-up";

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
                    Arguments.of("abcd", VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "loginId", "abcd".length()),
                    Arguments.of("abcdefghijklm", VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS, "loginId", "abcdefghijklm".length()),
                    Arguments.of(VALID_LOGIN_ID, "abcdefg", VALID_PHONE, VALID_ADDRESS, "password", "abcdefg".length()),
                    Arguments.of(VALID_LOGIN_ID, "abcdefghijklmnop", VALID_PHONE, VALID_ADDRESS, "password", "abcdefghijklmnop".length())
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
            final String url = "/users/sign-up";

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
    public void signUp_실패_파라미터가_null() throws Exception {
        //given
        final String url = "/users/sign-up";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(null))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_실패_UserService에서_에러_Throw() throws Exception {
        //given
        final String url = "/users/sign_up";

        doThrow(new BusinessException("Duplicate User Registration Request"))
                .when(userService)
                .save(any(User.class));

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(createTestUserDto(VALID_LOGIN_ID, VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_성공() throws Exception {
        //given
        final String url = "/users/sign-up";

        doNothing().when(userService).save(any(User.class));

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(createTestUserDto(VALID_LOGIN_ID, VALID_PASSWORD, VALID_PHONE, VALID_ADDRESS)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isCreated());
    }
}
