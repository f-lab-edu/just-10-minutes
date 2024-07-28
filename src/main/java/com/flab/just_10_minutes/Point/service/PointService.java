package com.flab.just_10_minutes.Point.service;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointHistoryResponseDto;
import com.flab.just_10_minutes.Point.dto.PointStatusDto;
import com.flab.just_10_minutes.Point.infrastructure.PointDao;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import static com.flab.just_10_minutes.Util.Exception.Business.BusinessException.INIT_POINT_COULD_NOT_MINUS;


@Service
@RequiredArgsConstructor
public class PointService {

    private final UserDao userDao;
    private final PointDao pointDao;

    public PointHistory offerPoint(final PointHistory pointHistory) {
        userDao.fetch(pointHistory.getLoginId());

        PointHistory newHistory = pointDao.calculateTotalQuantity(pointHistory).orElseThrow(() -> {throw new BusinessException(INIT_POINT_COULD_NOT_MINUS);});
        pointDao.save(newHistory);

        return pointDao.findTopByOrderByLoginIdDesc(pointHistory.getLoginId());
    }

    public Long getTotalPoint(final String loginId) {
        userDao.fetch(loginId);

        PointHistory latestHistory = pointDao.findTopByOrderByLoginIdDesc(loginId);

        return latestHistory == null ? 0L : latestHistory.getTotalQuantity();
    }

    public PointStatusDto getPointHistories(final String loginId) {
        userDao.fetch(loginId);

        return PointStatusDto.builder()
                    .totalQuantity(getTotalPoint(loginId))
                    .histories(pointDao.findByLoginId(loginId)
                                        .stream()
                                        .map(v -> PointHistoryResponseDto.builder()
                                                                        .quantity(v.getQuantity())
                                                                        .reason(v.getReason())
                                                                        .build())
                                        .collect(Collectors.toList())).build();
    }
}
