package com.flab.just_10_minutes.Payment.gateway;

import com.flab.just_10_minutes.Payment.domain.Status;
import com.flab.just_10_minutes.Payment.dto.BillingRequestDto;
import com.flab.just_10_minutes.Payment.dto.IamportPaymentRequestDto;
import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.infrastructure.CustomerUidDao;
import com.flab.just_10_minutes.Payment.infrastructure.IamportApiClient;
import com.flab.just_10_minutes.Payment.infrastructure.PaymentResultDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CustomerUidDao customerUidDao;
    private final PaymentResultDao paymentResultDao;
    private final IamportApiClient iamportApiClient;

    public PaymentResult paymentTransaction(@Valid IamportPaymentRequestDto iamportPaymentRequestDto) {
        String customerUid = fetchCustomerUid(iamportPaymentRequestDto);
        PaymentResult paymentResult = PaymentResult.from(iamportApiClient.againPayment(IamportPaymentRequestDto.toAgainPaymentData(iamportPaymentRequestDto, customerUid)));

        if (paymentResult.getStatus().equals(Status.PAID)) {
            paymentResultDao.save(paymentResult);
        } else if (paymentResult.getStatus().equals(Status.FAIL)) {
            throw new BusinessException("Fail to Payment : " + paymentResult.getFailReason());
        }

        return fetch(paymentResult.getImpUid());
    }

    public PaymentResult fetch(final String impUid) {
        return paymentResultDao.fetchByImpUid(impUid);
    }

    private String issueCustomerUid(final String loginId, @Valid BillingRequestDto billingData) {
        String customerUid = iamportApiClient.issueCustomerUid(billingData);
        customerUidDao.save(loginId, customerUid);
        return customerUid;
    }

    private String fetchCustomerUid(final IamportPaymentRequestDto paymentDataDto) {
        Optional<String> OptionalCustomerUid = customerUidDao.findByLoginId(paymentDataDto.getCustomerLoginId());
        return OptionalCustomerUid.orElse(issueCustomerUid(paymentDataDto.getCustomerLoginId(), paymentDataDto.getBillingRequestDto()));
    }
}
