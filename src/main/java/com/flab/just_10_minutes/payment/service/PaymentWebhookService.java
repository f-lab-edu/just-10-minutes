package com.flab.just_10_minutes.payment.service;

import com.flab.just_10_minutes.payment.domain.PaymentResult;
import com.flab.just_10_minutes.payment.dto.PortOnePaidWebhookRequest;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.IamportApiClient;
import com.flab.just_10_minutes.payment.infrastructure.entity.PaymentResultEntity;
import com.flab.just_10_minutes.payment.infrastructure.repository.PaymentResultDao;
import com.flab.just_10_minutes.util.alarm.slack.SlackClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.PAID;
import static com.flab.just_10_minutes.util.alarm.slack.SlackMessage.createDiffersStatus;
import static com.flab.just_10_minutes.util.alarm.slack.SlackMessage.createImpUidMissingInternal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentResultDao paymentResultDao;

    private final IamportApiClient iamportApiClient;
    private final SlackClient slackClient;

    public void validatePaidWebhook(PortOnePaidWebhookRequest iamportWebhookDto) {
        PaymentResult portOnePaymentResult = PaymentResult.from(iamportApiClient.fetchPayment(iamportWebhookDto.getImpUid()));

        if (portOnePaymentResult.getStatus() != PAID) {
            log.error("PortOne Webhook error occurred : Webhook Status is not equal to PortOne Server's Status");
            return;
        }

        Optional<PaymentResultEntity> optPaymentResult = paymentResultDao.findWithOrderByImpUid(iamportWebhookDto.getImpUid(), iamportWebhookDto.getStatus().getLable());
        PaymentResult existPaymentResult = optPaymentResult.map(PaymentResultEntity::toDomain).orElse(null);

        if (existPaymentResult == null) {
            log.error("PortOne Webhook error occurred : Provided ImpUid [" + portOnePaymentResult.getImpUid() + "] not found in Internal");
            slackClient.sendMessage(createImpUidMissingInternal(iamportWebhookDto.getImpUid()));
            return;
        }

        if (existPaymentResult.getStatus() != PAID) {
            log.error("PortOne Webhook error occurred : Provided ImpUid [" + portOnePaymentResult.getImpUid() + "]'s status differs from Internal");
            slackClient.sendMessage(createDiffersStatus(iamportWebhookDto.getImpUid(), existPaymentResult.getStatus()));
        }
    }
}
