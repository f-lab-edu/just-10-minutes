package com.flab.just_10_minutes.message.fcm.infrastructure.repository;

import com.flab.just_10_minutes.message.fcm.domain.FcmMessage;
import com.flab.just_10_minutes.message.fcm.infrastructure.entity.FcmMessageEntity;
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
public class FcmMessageDao {

    private final FcmMessageMapper fcmMessageMapper;

    public FcmMessage save(FcmMessage fcmMessage) {
        int saveResult = fcmMessageMapper.save(FcmMessageEntity.from(fcmMessage));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }

        return fcmMessage;
    }

    private Optional<FcmMessageEntity> findByMessageId(final String messageId) {
        return Optional.ofNullable(fcmMessageMapper.findByMessageId(messageId));
    }

    public FcmMessage fetchByMessageId(final String messageId) {
        FcmMessageEntity fcmMessageEntity = findByMessageId(messageId).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, CAMPAIGN);
        });

        return FcmMessageEntity.toDomain(fcmMessageEntity);
    }

    public void patch(FcmMessage fcmMessage) {
        int updateResult = fcmMessageMapper.patch(FcmMessageEntity.from(fcmMessage));
        if (updateResult != 1) {
            throw new InternalException(FAIL_TO_UPDATE);
        }
    }
}
