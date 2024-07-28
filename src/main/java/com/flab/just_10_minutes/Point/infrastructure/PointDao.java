package com.flab.just_10_minutes.Point.infrastructure;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.InternalException.FAIL_TO_INSERT;

@Repository
@RequiredArgsConstructor
public class PointDao {

    private final PointMapper pointMapper;

    public void save(final PointHistory pointHistory) {
        int insertCount = pointMapper.save(pointHistory);

        if (insertCount != 1) {
            throw new InternalException(FAIL_TO_INSERT);
        }
    }

    public PointHistory findTopByOrderByLoginIdDesc(final String loginId) {
        return pointMapper.findTopByOrderByLoginIdDesc(loginId);
    }

    public List<PointHistory> findByLoginId(final String loginId) {
        return pointMapper.findByLoginId(loginId);
    }

    public Optional<PointHistory> calculateTotalQuantity(@NonNull PointHistory newHistory) {
        PointHistory latestHistory = findTopByOrderByLoginIdDesc(newHistory.getLoginId());
        return Optional.ofNullable(newHistory.calculateTotalQuantity(latestHistory));
    }
}
