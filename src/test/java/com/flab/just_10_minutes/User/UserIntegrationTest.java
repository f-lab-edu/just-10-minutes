package com.flab.just_10_minutes.User;

import com.google.gson.Gson;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private Gson gson = new Gson();

    @Test
    public void signUp_실패_이미_존재하는_회원() throws Exception {
        //given
        final String url = "/users/sign_up";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(createTestUserDto(EXIST_ID, PASSWORD, PHONE, ADDRESS)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void signUp_성공() throws Exception {
        //given
        final String url = "/users/sign_up";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(createTestUserDto(NOT_EXIST_ID, PASSWORD, PHONE, ADDRESS)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isCreated());
    }

    @Test
    public void showUserProfile_실패_존재하지_않는_회원() throws Exception {
        //given
        final String url = "/users/profile/{loginId}";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, NOT_EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isBadRequest());
    }

    @Test
    public void showUserProfile_성공() throws Exception {
        //given
        final String url = "/users/profile/{loginId}";

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isOk());
    }
}
