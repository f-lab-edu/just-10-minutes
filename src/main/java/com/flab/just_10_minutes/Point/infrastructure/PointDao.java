package com.flab.just_10_minutes.Point.infrastructure;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
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

    public Optional<PointHistory> findFirst(final String loginId) {
        return Optional.ofNullable(pointMapper.findTopByOrderByLoginIdDesc(loginId));
    }

    public List<PointHistory> findByLoginId(final String loginId) {
        return pointMapper.findByLoginId(loginId);
    }
}
