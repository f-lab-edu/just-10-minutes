package com.flab.just_10_minutes.Payment.infrastructure;

import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;

@Repository
@RequiredArgsConstructor
public class CustomerUidDao {

    private final BillingKeyMapper customUidMapper;

    public void save(final String loginId, final String customerUid) {
        int saveResult = customUidMapper.save(BillingKeyEntity.from(loginId, customerUid));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    public Optional<String> findByLoginId(final String loginId) {
        return Optional.ofNullable(customUidMapper.findByLoginId(loginId));
    }
}
