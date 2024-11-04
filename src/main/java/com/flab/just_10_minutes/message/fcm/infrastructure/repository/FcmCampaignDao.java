package com.flab.just_10_minutes.message.fcm.infrastructure.repository;

import com.flab.just_10_minutes.message.fcm.domain.FcmCampaign;
import com.flab.just_10_minutes.message.fcm.infrastructure.entity.FcmCampaignEntity;
import com.flab.just_10_minutes.util.exception.database.InternalException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.CAMPAIGN;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class FcmCampaignDao {

    private final FcmCampaignMapper fcmCampaignMapper;

    public void save(FcmCampaign fcmCampaign) {
        int saveResult = fcmCampaignMapper.save(FcmCampaignEntity.from(fcmCampaign));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    private Optional<FcmCampaignEntity> findById(final Long id) {
        return Optional.ofNullable(fcmCampaignMapper.findByLoginId(id));
    }

    public FcmCampaign fetchById(final Long id) {
        FcmCampaignEntity fcmCampaignEntity = findById(id).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, CAMPAIGN);
        });

        return FcmCampaignEntity.toDomain(fcmCampaignEntity);
    }
}
