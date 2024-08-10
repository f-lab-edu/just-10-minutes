package com.flab.just_10_minutes.Payment.gateway;

import com.flab.just_10_minutes.Payment.domain.BillingData;
import com.flab.just_10_minutes.Payment.dto.PaymentDataDto;
import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.infrastructure.BillingKeyDao;
import com.flab.just_10_minutes.Payment.infrastructure.PaymentResultDao;
import com.siot.IamportRestClient.exception.IamportResponseException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.flab.just_10_minutes.Util.Config.IamportConfig.iamportClient;

@Component
@RequiredArgsConstructor
public class PaymentGateWay {

    private final BillingKeyDao billingKeyDao;
    private final PaymentResultDao paymentResultDao;

    public PaymentResult paymentTransaction(@Valid PaymentDataDto paymentDataDto) {
        String billingKey = null;
        PaymentResult paymentResult = null;

        try {
            billingKey = fetchBillingKey(paymentDataDto);

            paymentResult = PaymentResult.from(iamportClient.againPayment(PaymentDataDto.toPaymentData(paymentDataDto, billingKey)).getResponse());
        } catch (IOException ie) {
            throw new RuntimeException("Network Error");
        } catch (IamportResponseException ire) {
            throw new RuntimeException("iamport exception");
        }

        if (paymentResult.getStatus().equals("paid")) {
            paymentResultDao.save(paymentResult);
        } else if (paymentResult.getStatus().equals("fail")) {
            throw new RuntimeException("Fail to Payment");
        }

        return fetch(paymentResult.getPaymentTxId());
    }

    public PaymentResult fetch(final String paymentTxId) {
        return paymentResultDao.fetchByPaymentTxId(paymentTxId);
    }

    private String issueBillingKey(final String loginId, @Valid BillingData billingData) throws IamportResponseException, IOException {
        String billingKey = iamportClient.postBillingCustomer(UUID.randomUUID().toString(), BillingData.toBillingCustomerData(billingData))
                    .getResponse()
                    .getCustomerUid();

        billingKeyDao.save(loginId, billingKey);

        return billingKey;
    }

    private String fetchBillingKey(final PaymentDataDto paymentDataDto) throws IamportResponseException, IOException {
        Optional<String> OptionalBillingKey = billingKeyDao.findByLoginId(paymentDataDto.getCustomerLoginId());

        String billingKey = OptionalBillingKey.orElse(issueBillingKey(paymentDataDto.getCustomerLoginId(), paymentDataDto.getBillingData()));

        return billingKey;
    }
}
