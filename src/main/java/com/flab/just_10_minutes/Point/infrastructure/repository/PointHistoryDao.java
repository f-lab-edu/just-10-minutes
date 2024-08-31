package com.flab.just_10_minutes.Point.infrastructure.repository;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.infrastructure.entity.PointHistoryEntity;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.NOT_FOUND;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.USER;

@Repository
@RequiredArgsConstructor
public class PointHistoryDao {

    private final PointHistoryMapper pointHistoryMapper;

    public void save(final PointHistory pointHistory) {
        int insertCount = pointHistoryMapper.save(PointHistoryEntity.from(pointHistory));

        if (insertCount != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    private Optional<PointHistoryEntity> findFirst(final String loginId) {
        return Optional.ofNullable(pointHistoryMapper.findTopByOrderByLoginIdDesc(loginId));
    }

    public List<PointHistory> findByLoginId(final String loginId) {
        return pointHistoryMapper.findByLoginId(loginId);
    }

    public PointHistory fetchFirst(final String loginId) {
        PointHistoryEntity pointHistoryEntity = findFirst(loginId).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, USER + "'s PointHistory");
        });
        return PointHistoryEntity.toDomain(pointHistoryEntity);
    }
}
