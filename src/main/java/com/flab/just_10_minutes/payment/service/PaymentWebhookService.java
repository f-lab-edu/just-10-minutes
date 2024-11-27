package com.flab.just_10_minutes.payment.service;

import com.flab.just_10_minutes.payment.domain.PaymentResult;
import com.flab.just_10_minutes.payment.dto.PortOnePaidWebhookRequest;
import com.flab.just_10_minutes.payment.infrastructure.Iamport.IamportApiClient;
import com.flab.just_10_minutes.payment.infrastructure.entity.PaymentResultEntity;
import com.flab.just_10_minutes.payment.infrastructure.repository.PaymentResultDao;
import com.flab.just_10_minutes.util.alarm.slack.SlackClient;
import com.flab.just_10_minutes.util.exception.iamport.IamportException;
import com.flab.just_10_minutes.util.exception.webhook.WebhookException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.flab.just_10_minutes.payment.domain.PaymentResultStatus.PAID;
import static com.flab.just_10_minutes.util.alarm.slack.SlackMessage.createDiffersStatus;
import static com.flab.just_10_minutes.util.alarm.slack.SlackMessage.createImpUidMissingInternal;
import static com.flab.just_10_minutes.util.exception.webhook.WebHookMessage.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentResultDao paymentResultDao;

    private final IamportApiClient iamportApiClient;
    private final SlackClient slackClient;

    public void validatePaidWebhook(PortOnePaidWebhookRequest iamportWebhookDto) {
        try {
            PaymentResult portOnePaymentResult = PaymentResult.from(iamportApiClient.fetchPayment(iamportWebhookDto.getImpUid()));
            if (portOnePaymentResult.getStatus() != PAID) {
                throw new WebhookException(DIFFER_STATUS, null);
            }

            PaymentResult paymentResult = paymentResultDao.findWithOrderByImpUid(iamportWebhookDto.getImpUid(), iamportWebhookDto.getStatus().getLable())
                                                            .map(PaymentResultEntity::toDomain)
                                                            .orElseThrow(() -> new WebhookException(NOT_FOUND, createImpUidMissingInternal(iamportWebhookDto.getImpUid())));
            if (paymentResult.getStatus() != PAID) {
                throw new WebhookException(DIFFER_STATUS, createDiffersStatus(iamportWebhookDto.getImpUid(), paymentResult.getStatus()));
            }
        } catch (IamportException ie) {
            log.error("Not Found payment results [ImpUid] : " + iamportWebhookDto.getImpUid());
        } catch (WebhookException we) {
            log.error(we.getMessage() + iamportWebhookDto.getImpUid());

            if (we.getSlackMessage() != null) {
                slackClient.sendMessage(we.getSlackMessage());
            }
        }
    }
}
