package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.domain.FcmNotification;
import com.flab.just_10_minutes.notification.infrastructure.entity.FcmNotificationEntity;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import com.flab.just_10_minutes.common.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.common.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.CAMPAIGN;
import static com.flab.just_10_minutes.common.exception.database.NotFoundException.NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class FcmNotificationDao {

    private final FcmNotificationMapper notificationMapper;

    public FcmNotification save(FcmNotification fcmNotification) {
        int saveResult = notificationMapper.save(FcmNotificationEntity.from(fcmNotification));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }

        return fetchByEventId(fcmNotification.getNotificationId());
    }

    private Optional<FcmNotificationEntity> findByEventId(final String eventId) {
        return Optional.ofNullable(notificationMapper.findByNotificationId(eventId));
    }

    public FcmNotification fetchByEventId(final String eventId) {
        FcmNotificationEntity notificationEntity = findByEventId(eventId).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, CAMPAIGN);
        });

        return FcmNotificationEntity.toDomain(notificationEntity);
    }
}
