package com.flab.just_10_minutes.payment.infrastructure.repository;

import com.flab.just_10_minutes.payment.infrastructure.entity.BillingKeyEntity;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.common.exception.database.InternalException.FAIL_TO_INSERT;

@Repository
@RequiredArgsConstructor
public class BillingKeyDao {

    private final BillingKeyMapper billingKeyMapper;

    public void save(final String loginId, final String customerUid) {
        int saveResult = billingKeyMapper.save(BillingKeyEntity.from(loginId, customerUid));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    public Optional<String> findByLoginId(final String loginId) {
        return Optional.ofNullable(billingKeyMapper.findByLoginId(loginId));
    }
}
