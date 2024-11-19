package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.domain.NotificationEvent;
import com.flab.just_10_minutes.notification.infrastructure.entity.NotificationEventEntity;
import com.flab.just_10_minutes.util.exception.database.InternalException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_UPDATE;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.*;

@Repository
@RequiredArgsConstructor
public class NotificationEventDao {

    private final NotificationEventMapper notificationEventMapper;

    public NotificationEvent save(NotificationEvent event) {
        int saveResult = notificationEventMapper.save(NotificationEventEntity.from(event));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }

        return fetchByEventId(event.getEventId());
    }

    private Optional<NotificationEventEntity> findByEventId(final String eventId) {
        return Optional.ofNullable(notificationEventMapper.findByMessageId(eventId));
    }

    public NotificationEvent fetchByEventId(final String eventId) {
        NotificationEventEntity eventEntity = findByEventId(eventId).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, CAMPAIGN);
        });

        return NotificationEventEntity.toDomain(eventEntity);
    }

    public void patch(NotificationEvent notification) {
        int updateResult = notificationEventMapper.patch(NotificationEventEntity.from(notification));
        if (updateResult != 1) {
            throw new InternalException(FAIL_TO_UPDATE);
        }
    }
}
