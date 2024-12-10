package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.domain.FcmToken;
import com.flab.just_10_minutes.notification.infrastructure.entity.FcmTokenEntity;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import com.flab.just_10_minutes.common.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.common.exception.database.InternalException.FAIL_TO_DELETE;
import static com.flab.just_10_minutes.common.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.NOT_FOUND;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.TOKEN;

@Repository
@RequiredArgsConstructor
public class FcmTokenDao {

    private final FcmTokenMapper fcmTokenMapper;

    public void save(FcmToken fcmToken) {
        int saveResult = fcmTokenMapper.upsert(FcmTokenEntity.from(fcmToken));
        if (saveResult < 1 || saveResult > 2) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    private Optional<FcmTokenEntity> findByLoginId(final String loginId) {
        return Optional.ofNullable(fcmTokenMapper.findByLoginId(loginId));
    }

    public FcmToken fetchByLoginId(final String loginId) {
        FcmTokenEntity fcmTokenEntity = findByLoginId(loginId).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, TOKEN);
        });

        return FcmTokenEntity.toDomain(fcmTokenEntity);
    }

    public void delete(final String fcmToken) {
        int deleteResult = fcmTokenMapper.delete(fcmToken);
        if (deleteResult != 1) {
            throw new InternalException(FAIL_TO_DELETE);
        }
    }
}
