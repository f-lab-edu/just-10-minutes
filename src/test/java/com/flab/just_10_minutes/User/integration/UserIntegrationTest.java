package com.flab.just_10_minutes.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.User.infrastructure.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.User.UserDtoTestFixture.*;
import static com.flab.just_10_minutes.User.UserTestFixture.createUser;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    static final String SIGN_UP_URL = "/users/sign-up";
    private static final String PROFILE_URL = "/users/profile/{loginId}";

    @Autowired
    private UserMapper userMapper;

    private UserDao target;

    @BeforeEach
    public void setUp() {
        target = new UserDao(userMapper);
    }

    public void saveUser(String loginId) {
        User user = createUser(loginId);
        target.save(user);
    }

    @Test
    public void signUp_실패_이미_존재하는_회원() throws Exception {
        //setUp
        saveUser(EXIST_ID);
        //given
        final String url = SIGN_UP_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestUserDto(EXIST_ID, PASSWORD, PHONE, ADDRESS)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_성공() throws Exception {
        //given
        final String url = SIGN_UP_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestUserDto(NOT_EXIST_ID, PASSWORD, PHONE, ADDRESS)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isCreated());
    }

    @Test
    public void showUserProfile_실패_존재하지_않는_회원() throws Exception {
        //given
        final String url = PROFILE_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, NOT_EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isNotFound());
    }

    @Test
    public void showUserProfile_성공() throws Exception {
        //setUp
        saveUser(EXIST_ID);
        //given
        final String url = PROFILE_URL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isOk());
    }
}
