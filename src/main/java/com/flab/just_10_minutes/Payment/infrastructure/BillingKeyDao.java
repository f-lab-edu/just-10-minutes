package com.flab.just_10_minutes.Payment.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BillingKeyDao {

    private final BillingKeyMapper billingKeyMapper;

    public void save(final String loginId, final String billingKey) {
        int saveResult = billingKeyMapper.save(loginId, billingKey);
        if (saveResult != 1) {
            throw new RuntimeException();
        }
    }

    public Optional<String> findByLoginId(final String loginId) {
        return Optional.ofNullable(billingKeyMapper.findByLoginId(loginId));
    }
}
