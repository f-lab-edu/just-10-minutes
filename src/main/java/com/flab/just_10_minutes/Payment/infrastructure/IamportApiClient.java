package com.flab.just_10_minutes.Payment.infrastructure;

import com.flab.just_10_minutes.Payment.dto.BillingRequestDto;
import com.flab.just_10_minutes.Util.Config.IamportConfig;
import com.flab.just_10_minutes.Util.Exception.Iamport.IamportException;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.AgainPaymentData;
import com.siot.IamportRestClient.response.Payment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;

import static com.flab.just_10_minutes.Util.Common.IDUtil.issueCustomUid;

@Component
@RequiredArgsConstructor
public class IamportApiClient {

    private final IamportClient iamportClient;
    private final IamportConfig iamportConfig;

    public String issueCustomerUid(@Valid BillingRequestDto billingRequestDto) {
        String customerUid = null;
        try {
            customerUid = iamportClient.postBillingCustomer(issueCustomUid(), BillingRequestDto.toBillingCustomerData(billingRequestDto, iamportConfig.getPg(billingRequestDto.getPg())))
                                        .getResponse()
                                        .getCustomerUid();
        } catch (IOException | IamportResponseException e) {
            throw new IamportException(e);
        }
        return customerUid;
    }

    public Payment againPayment(AgainPaymentData againPaymentData) {
        Payment payment = null;
        try {
            payment = iamportClient.againPayment(againPaymentData).getResponse();
        } catch (IOException | IamportResponseException e) {
            throw new IamportException(e);
        }
        return payment;
    }
}
