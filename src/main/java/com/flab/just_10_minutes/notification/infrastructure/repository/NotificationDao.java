package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.domain.ChannelType;
import com.flab.just_10_minutes.notification.domain.Notification;
import com.flab.just_10_minutes.notification.infrastructure.entity.NotificationEntity;
import com.flab.just_10_minutes.util.exception.database.InternalException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_UPDATE;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.CAMPAIGN;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class NotificationDao {

    private final NotificationMapper notificationMapper;

    public Notification save(Notification notification) {
        int saveResult = notificationMapper.save(NotificationEntity.from(notification));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }

        return fetchByEventIdAndChannelType(notification.getEventId(), notification.getChannelType());
    }

    private Optional<NotificationEntity> findByEventIdAndChannelType(final String eventId, final ChannelType channelType) {
        return Optional.ofNullable(notificationMapper.findByEventIdAndChannelType(eventId, channelType));
    }

    public Notification fetchByEventIdAndChannelType(final String eventId, final ChannelType channelType) {
        NotificationEntity notificationEntity = findByEventIdAndChannelType(eventId, channelType).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, CAMPAIGN);
        });

        return NotificationEntity.toDomain(notificationEntity);
    }

    public void patch(Notification notification) {
        int updateResult = notificationMapper.patch(NotificationEntity.from(notification));
        if (updateResult != 1) {
            throw new InternalException(FAIL_TO_UPDATE);
        }
    }
}
