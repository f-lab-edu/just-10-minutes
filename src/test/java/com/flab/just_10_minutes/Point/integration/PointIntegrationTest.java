package com.flab.just_10_minutes.Point.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointHistories;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.flab.just_10_minutes.Point.fixture.PointHistoryDtoTestFixture.REASON;
import static com.flab.just_10_minutes.Point.fixture.PointHistoryDtoTestFixture.createTestPointHistoryDto;
import static com.flab.just_10_minutes.User.fixture.UserDtoTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@Slf4j
public class PointIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private static final String OFFER = "/points/offer";
    private static final String TOTAL = "/points/total/{loginId}";
    private static final String HISTORIES = "/points/histories/{loginId}";



    private void saveUser(String loginId) throws Exception {
        final ResultActions resultAction = mockMvc.perform(
            MockMvcRequestBuilders.post("/users/sign-up")
                    .content(
                            mapper.writeValueAsString(createTestUserDto(loginId, PASSWORD, PHONE, ADDRESS)))
                    .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void saveHistory(String loginId, Long quantity) throws Exception {
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(OFFER)
                        .content(mapper.writeValueAsString(createTestPointHistoryDto(loginId, quantity, REASON)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Test
    public void offer_실패_존재하지_않는_회원() throws Exception {
        //given
        final String url = OFFER;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestPointHistoryDto(NOT_EXIST_ID, 100L, REASON)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultAction.andExpect(status().isNotFound());
    }

    @Test
    public void offer_성공_최초_기록() throws Exception {
        //setUp
        saveUser(NOT_EXIST_ID);

        //given
        final String url = OFFER;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestPointHistoryDto(NOT_EXIST_ID, 100L, REASON)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then

        resultAction.andExpect(status().isOk());
        String content = resultAction.andReturn().getResponse().getContentAsString();
        PointHistory historyResponse = mapper.readValue(content, PointHistory.class);
        assertThat(historyResponse.getTotalQuantity()).isEqualTo(100L);
    }

    @Test
    public void offer_성공_기록이_있는_경우() throws Exception {
        //setUp
        saveUser(NOT_EXIST_ID);
        saveHistory(NOT_EXIST_ID, 100L);
        saveHistory(NOT_EXIST_ID, 200L);
        saveHistory(NOT_EXIST_ID, -200L);

        //given
        final String url = OFFER;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(mapper.writeValueAsString(createTestPointHistoryDto(NOT_EXIST_ID, 100L, REASON)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then

        resultAction.andExpect(status().isOk());

        String content = resultAction.andReturn().getResponse().getContentAsString();
        PointHistory historyResponse = mapper.readValue(content, PointHistory.class);
        assertThat(historyResponse.getTotalQuantity()).isEqualTo(200L);
    }

    @Test
    public void showTotal_실패_존재하지_않는_회원() throws Exception {
        //given
        final String url = TOTAL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, NOT_EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isNotFound());
    }

    @Test
    public void showTotal_성공() throws Exception {
        //setUp
        saveUser(EXIST_ID);
        saveHistory(EXIST_ID, 100L);
        saveHistory(EXIST_ID, 200L);

        //given
        final String url = TOTAL;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isOk());
        String content = resultAction.andReturn().getResponse().getContentAsString();
        Long response = mapper.readValue(content, Long.class);
        assertThat(response).isEqualTo(300L);
    }

    @Test
    public void showHistories_실패_존재하지_않는_회원() throws Exception {
        //given
        final String url = HISTORIES;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, NOT_EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isNotFound());
    }

    @Test
    public void showHistories_성공() throws Exception {
        //setUp
        saveUser(EXIST_ID);
        saveHistory(EXIST_ID, 100L);
        saveHistory(EXIST_ID, -200L);
        saveHistory(EXIST_ID, 300L);
        //given
        final String url = HISTORIES;

        //when
        final ResultActions resultAction = mockMvc.perform(
                MockMvcRequestBuilders.get(url, EXIST_ID)
        );

        //then
        resultAction.andExpect(status().isOk());

        String content = resultAction.andReturn().getResponse().getContentAsString();
        PointHistories response = mapper.readValue(content, PointHistories.class);
        assertThat(response.getTotalQuantity()).isEqualTo(200L);
        assertThat(response.getHistories().size()).isEqualTo(3);
    }
}
