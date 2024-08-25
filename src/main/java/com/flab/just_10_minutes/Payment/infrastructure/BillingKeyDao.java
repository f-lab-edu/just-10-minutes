package com.flab.just_10_minutes.Payment.infrastructure;

import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;

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
