package com.flab.just_10_minutes.payment.infrastructure.Iamport;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.just_10_minutes.payment.dto.BillingRequest;
import com.flab.just_10_minutes.payment.dto.PaymentRequest;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.request.IamportAgainPaymentData;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.request.IamportAuthData;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.request.IamportBillingCustomerData;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.response.IamportAccessToken;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.response.IamportBillingCustomer;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.response.IamportResponse;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.response.IamportPayment;
import com.flab.just_10_minutes.util.exception.iamport.IamportException;
import com.flab.just_10_minutes.util.iamport.IamportConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static com.flab.just_10_minutes.util.common.IDUtil.issueCustomUid;

@Component
@RequiredArgsConstructor
@Slf4j
public class IamportApiClient {

    private final IamportConfig iamportConfig;
    private final RestClient restClient;

    public IamportAccessToken issueToken() {
        IamportResponse<IamportAccessToken> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/users/getToken").build())
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

    public String issueCustomerUid(BillingRequest billingRequest) {
        IamportAccessToken accessToken = issueToken();
        IamportBillingCustomerData billingCustomerData = BillingRequest.toIamportBillingCustomerData(billingRequest, iamportConfig.getPg(billingRequest.getPg()));

        IamportResponse<IamportBillingCustomer> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/subscribe/customers/").path(issueCustomUid()).build())
                .header("Authorization", accessToken.getAccessToken())
                .body(billingCustomerData)
                .exchange((req, res) -> new ObjectMapper().readValue(res.getBody(), new TypeReference<IamportResponse<IamportBillingCustomer>>() {}));

        if (response.getCode() == -1) {
            throw new IamportException(response.getMessage());
        }
        return response.getResponse().getCustomerUid();
    }

    public IamportPayment againPayment(PaymentRequest paymentRequest, String customerUid) {
        IamportAgainPaymentData iamportAgainPaymentData = PaymentRequest.toAgainPaymentData(paymentRequest, customerUid);
        IamportAccessToken accessToken = issueToken();

        IamportResponse<IamportPayment> response = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/subscribe/payments/again").build())
                .header("Authorization", accessToken.getAccessToken())
                .body(iamportAgainPaymentData)
                .exchange((req, res) -> new ObjectMapper().readValue(res.getBody(), new TypeReference<IamportResponse<IamportPayment>>() {}));

        if (response.getCode() == -1) {
            throw new IamportException(response.getMessage());
        }
        return response.getResponse();
    }
}