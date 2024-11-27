package com.flab.just_10_minutes.payment.service;

import com.flab.just_10_minutes.payment.dto.BillingRequest;
import com.flab.just_10_minutes.payment.dto.PaymentRequest;
import com.flab.just_10_minutes.payment.domain.PaymentResult;
import com.flab.just_10_minutes.payment.infrastructure.repository.BillingKeyDao;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.IamportApiClient;
import com.flab.just_10_minutes.payment.infrastructure.repository.PaymentResultDao;
import com.flab.just_10_minutes.util.exception.business.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.FAIL;
import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.PAID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BillingKeyDao billingKeyDao;
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
        billingKeyDao.save(loginId, customerUid);
        return customerUid;
    }

    private String fetchCustomerUid(PaymentRequest paymentDataDto) {
        Optional<String> OptionalCustomerUid = billingKeyDao.findByLoginId(paymentDataDto.getCustomerLoginId());
        return OptionalCustomerUid.orElseGet(() -> issueCustomerUid(paymentDataDto.getCustomerLoginId(), paymentDataDto.getBillingRequest()));
    }
}
