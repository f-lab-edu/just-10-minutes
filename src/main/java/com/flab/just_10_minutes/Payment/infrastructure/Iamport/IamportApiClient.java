package com.flab.just_10_minutes.Payment.infrastructure.Iamport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.Payment.dto.BillingRequestDto;
import com.flab.just_10_minutes.Payment.dto.PaymentRequestDto;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.request.IamportAgainPaymentData;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.request.IamportAuthData;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.request.IamportBillingCustomerData;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.response.IamportAccessToken;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.response.IamportBillingCustomer;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.response.IamportResponse;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.response.IamportPayment;
import com.flab.just_10_minutes.Util.Exception.Iamport.IamportException;
import com.flab.just_10_minutes.Util.Iamport.IamportConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static com.flab.just_10_minutes.Util.Common.IDUtil.issueCustomUid;

@Component
@RequiredArgsConstructor
@Slf4j
public class IamportApiClient {

    private final IamportConfig iamportConfig;
    private final RestClient restClient;

    public IamportAccessToken issueToken() {
        IamportResponse<IamportAccessToken> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/getToken").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(IamportAuthData.builder()
                                    .impKey(iamportConfig.getApiKey())
                                    .impSecret(iamportConfig.getApiSecret())
                                    .build())
                .exchange((req, res) -> new ObjectMapper().readValue(res.getBody(), new TypeReference<IamportResponse<IamportAccessToken>>(){}));

        if (response.getCode() == -1) {
            throw new IamportException(response.getMessage());
        }
        return response.getResponse();
    }

    public String issueCustomerUid(BillingRequestDto billingRequestDto) {
        IamportAccessToken accessToken = issueToken();
        IamportBillingCustomerData billingCustomerData = BillingRequestDto.toIamportBillingCustomerData(billingRequestDto, iamportConfig.getPg(billingRequestDto.getPg()));

        IamportResponse<IamportBillingCustomer> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/subscribe/customers/").path(issueCustomUid()).build())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken.getAccessToken())
                .body(billingCustomerData)
                .exchange((req, res) -> new ObjectMapper().readValue(res.getBody(), new TypeReference<IamportResponse<IamportBillingCustomer>>() {}));

        if (response.getCode() == -1) {
            throw new IamportException(response.getMessage());
        }
        return response.getResponse().getCustomerUid();
    }

    public IamportPayment againPayment(PaymentRequestDto paymentRequestDto, String customerUid) {
        IamportAgainPaymentData iamportAgainPaymentData = PaymentRequestDto.toAgainPaymentData(paymentRequestDto, customerUid);
        IamportAccessToken accessToken = issueToken();

        IamportResponse<IamportPayment> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/subscribe/payments/again").build())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken.getAccessToken())
                .body(iamportAgainPaymentData)
                .exchange((req, res) -> new ObjectMapper().readValue(res.getBody(), new TypeReference<IamportResponse<IamportPayment>>() {}));

        if (response.getCode() == -1) {
            throw new IamportException(response.getMessage());
        }
        return response.getResponse();
    }
}
