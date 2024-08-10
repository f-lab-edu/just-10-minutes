package com.flab.just_10_minutes.Payment.infrastructure;

import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentResultDao {

    private final PaymentResultMapper paymentResultMapper;

    public void save(PaymentResult paymentResult) {
        int saveResult = paymentResultMapper.save(paymentResult);
        if (saveResult != 1) {
            throw new InternalException("Insert Fail");
        }
    }

    public PaymentResult fetchByPaymentTxId(final String paymentTxId) {
       return findByPaymentTxId(paymentTxId).orElseThrow(() -> {throw new RuntimeException("Not Exist");});
    }

    public Optional<PaymentResult> findByPaymentTxId(final String paymentTxId) {
        Optional<PaymentResult> byPaymentTxId = Optional.ofNullable(paymentResultMapper.findByPaymentTxId(paymentTxId));
        return byPaymentTxId;
    }
}
