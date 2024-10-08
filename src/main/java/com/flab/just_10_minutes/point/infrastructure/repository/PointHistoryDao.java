package com.flab.just_10_minutes.point.infrastructure.repository;

import com.flab.just_10_minutes.point.domain.PointHistory;
import com.flab.just_10_minutes.point.infrastructure.entity.PointHistoryEntity;
import com.flab.just_10_minutes.util.exception.database.InternalException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.NOT_FOUND;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.USER;

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
        return pointHistoryMapper.findByLoginId(loginId).stream()
                                                        .map(e -> PointHistoryEntity.toDomain(e))
                                                        .collect(Collectors.toList());
    }

    public PointHistory fetchFirst(final String loginId) {
        PointHistoryEntity pointHistoryEntity = findFirst(loginId).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, USER + "'s PointHistory");
        });
        return PointHistoryEntity.toDomain(pointHistoryEntity);
    }
}
