package com.flab.just_10_minutes.notification.infrastructure.repository;

import com.flab.just_10_minutes.notification.domain.Campaign;
import com.flab.just_10_minutes.notification.infrastructure.entity.CampaignEntity;
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
public class CampaignDao {

    private final CampaignMapper campaignMapper;

    public void save(Campaign fcmCampaign) {
        int saveResult = campaignMapper.save(CampaignEntity.from(fcmCampaign));
        if (saveResult != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    private Optional<CampaignEntity> findById(final Long id) {
        return Optional.ofNullable(campaignMapper.findByLoginId(id));
    }

    public Campaign fetchById(final Long id) {
        CampaignEntity campaignEntity = findById(id).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, CAMPAIGN);
        });

        return CampaignEntity.toDomain(campaignEntity);
    }
}
