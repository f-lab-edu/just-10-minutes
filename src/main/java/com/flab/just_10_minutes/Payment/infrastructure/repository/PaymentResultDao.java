package com.flab.just_10_minutes.Payment.infrastructure.repository;

import com.flab.just_10_minutes.Payment.domain.PaymentResult;
import com.flab.just_10_minutes.Payment.infrastructure.entity.PaymentResultEntity;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.IMP_UID;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PaymentResultDao {

    private final PaymentResultMapper paymentResultMapper;

    public void save(PaymentResult paymentResultDto) {
        int saveResult = paymentResultMapper.save(PaymentResultEntity.from(paymentResultDto));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    public PaymentResult fetchByImpUid(final String impUid) {
        PaymentResultEntity paymentResultEntity = findByImpUid(impUid).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, IMP_UID);
        });
        return PaymentResultEntity.to(paymentResultEntity);
    }

    public Optional<PaymentResultEntity> findByImpUid(final String impUid) {
        return Optional.ofNullable(paymentResultMapper.findByImpUid(impUid));
    }
}
