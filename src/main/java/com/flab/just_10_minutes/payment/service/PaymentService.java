package com.flab.just_10_minutes.payment.service;

import com.flab.just_10_minutes.payment.dto.BillingRequest;
import com.flab.just_10_minutes.payment.dto.IamportWebhookDto;
import com.flab.just_10_minutes.payment.dto.PaymentRequest;
import com.flab.just_10_minutes.payment.domain.PaymentResult;
import com.flab.just_10_minutes.payment.infrastructure.entity.PaymentResultEntity;
import com.flab.just_10_minutes.payment.infrastructure.repository.BillingKeyDao;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.IamportApiClient;
import com.flab.just_10_minutes.payment.infrastructure.repository.PaymentResultDao;
import com.flab.just_10_minutes.util.exception.business.BusinessException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.FAIL;
import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.PAID;
import static com.flab.just_10_minutes.util.alarm.slack.SlackUtil.sendMessage;

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

    public void validatePaidWebhook(final IamportWebhookDto iamportWebhookDto) {
        if (iamportWebhookDto.getStatus() != PAID) {
            log.error("PortOne Webhook error occurred : Webhook Status not Equals to PAID");
            return;
        }

        PaymentResult paymentResult = PaymentResult.from(iamportApiClient.fetchPayment(iamportWebhookDto.getImpUid()));

        if (paymentResult.getStatus() != PAID) {
            log.error("PortOne Webhook error occurred : Webhook Status not Equals to PortOne Server's Status");
            return;
        }

        Optional<PaymentResultEntity> optPaymentResult = paymentResultDao.findWithOrderByImpUidAndStatus(iamportWebhookDto.getImpUid(), iamportWebhookDto.getStatus().getLable());
        PaymentResult existPaymentResult = optPaymentResult.map(PaymentResultEntity::toDomain).orElse(null);

        if (existPaymentResult == null || existPaymentResult.getStatus() != PAID) {
            log.error("PortOne Webhook error occurred : Not Exist Payment Result or Order for the Provided ImpUid");
            sendMessage("결제 이상 발생", Stream.of(new Object[][] {
                            {"결제 금액", existPaymentResult == null ? "null" : existPaymentResult.getAmount().toString()},
                            {"포트원 결제 금액", paymentResult == null ? "null" : paymentResult.getAmount().toString()},
                            {"주문 ID", existPaymentResult == null ? "null" : existPaymentResult.getMerchantUid()},
                            {"결제 ID", iamportWebhookDto.getImpUid()}
                    })
                    .collect(Collectors.toMap(item -> (String) item[0], item -> (String) item[1])));
        }
    }
}
