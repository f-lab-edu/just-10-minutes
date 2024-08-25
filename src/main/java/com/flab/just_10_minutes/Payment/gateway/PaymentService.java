package com.flab.just_10_minutes.Payment.gateway;

import com.flab.just_10_minutes.Payment.dto.BillingRequest;
import com.flab.just_10_minutes.Payment.dto.PaymentRequest;
import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.infrastructure.BillingKeyDao;
import com.flab.just_10_minutes.Payment.infrastructure.Iamport.IamportApiClient;
import com.flab.just_10_minutes.Payment.infrastructure.PaymentResultDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.flab.just_10_minutes.Payment.domain.PaymentResultStatus.FAIL;
import static com.flab.just_10_minutes.Payment.domain.PaymentResultStatus.PAID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillingKeyDao customerUidDao;
    private final PaymentResultDao paymentResultDao;
    private final IamportApiClient iamportApiClient;

    public PaymentResult paymentTransaction(@Valid PaymentRequest paymentRequest) {
        String customerUid = fetchCustomerUid(paymentRequest);
        PaymentResult paymentResult = PaymentResult.from(iamportApiClient.againPayment(paymentRequest, customerUid));

        if (paymentResult.getStatus() == PAID) {
            paymentResultDao.save(paymentResult);
        } else if (paymentResult.getStatus() == FAIL) {
            throw new BusinessException("Fail to Payment : " + paymentResult.getFailReason());
        }

        return fetch(paymentResult.getImpUid());
    }

    public PaymentResult fetch(final String impUid) {
        return paymentResultDao.fetchByImpUid(impUid);
    }

    private String issueCustomerUid(final String loginId, @Valid BillingRequest billingRequest) {
        String customerUid = iamportApiClient.issueCustomerUid(billingRequest);
        customerUidDao.save(loginId, customerUid);
        return customerUid;
    }

    private String fetchCustomerUid(final PaymentRequest paymentDataDto) {
        Optional<String> OptionalCustomerUid = customerUidDao.findByLoginId(paymentDataDto.getCustomerLoginId());
        return OptionalCustomerUid.orElse(issueCustomerUid(paymentDataDto.getCustomerLoginId(), paymentDataDto.getBillingRequest()));
    }
}
