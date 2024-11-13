package com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.request.FcmApiV1Request;
import com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.response.FcmApiV1Response;
import com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.response.FcmApiV1FailResponse;
import com.flab.just_10_minutes.message.fcm.infrastructure.fcmAPiV1.response.FcmApiV1SuccessResponse;
import com.flab.just_10_minutes.util.message.fcm.FcmConfig;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmApiClient {

    private final FcmConfig fcmConfig;
    private final RestClient fcmRestClient;

    public FcmApiV1Response sendMessage(FcmMessage fcmMessage, FcmCampaign fcmCampaign)  {
        //TODO fcm response 에 따라 처리
        FcmApiV1Response response =
                fcmRestClient.post()
                .uri(uriBuilder -> uriBuilder.path(fcmConfig.getMessagePostfix()).build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .body(FcmApiV1Request.from(fcmMessage, fcmCampaign))
                .exchange((req, res) -> {
                    if (res.getStatusCode().value() > 200) {
                        FcmApiV1FailResponse failResponse = new ObjectMapper().readValue(res.getBody(), new TypeReference<FcmApiV1FailResponse>() {});
                        return FcmApiV1Response.withFailure(failResponse);
                    }

                    FcmApiV1SuccessResponse successResponse = new ObjectMapper().readValue(res.getBody(), new TypeReference<FcmApiV1SuccessResponse>() {});
                    return FcmApiV1Response.withSuccess(successResponse);
                    }
                );
        return response;
    }

    private String getAccessToken() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(fcmConfig.getCredentialsStream())
                    .createScoped(List.of(fcmConfig.getCredentialUrl()));
            credentials.refreshIfExpired();

            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
