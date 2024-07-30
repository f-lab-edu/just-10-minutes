package com.flab.just_10_minutes.Point.service;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointHistoryResponseDto;
import com.flab.just_10_minutes.Point.dto.PointStatusDto;
import com.flab.just_10_minutes.Point.infrastructure.PointDao;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PointService {

    private final UserDao userDao;
    private final PointDao pointDao;

    public PointHistory offerPoint(final PointHistory pointHistory) {
        User user = userDao.fetch(pointHistory.getLoginId());

        PointHistory newHistory = pointHistory.increase(user.getPoints());
        pointDao.save(newHistory);

        userDao.patchPoints(user.getLoginId(), newHistory.getTotalQuantity());

        return pointDao.findFirst(pointHistory.getLoginId()).orElseThrow(() -> {throw new BusinessException("Internal Error");});
    }

    public Long getTotalPoint(final String loginId) {
        User user = userDao.fetch(loginId);

        return user.getPoints();
    }

    public PointStatusDto getPointHistories(final String loginId) {
        User user = userDao.fetch(loginId);

        return PointStatusDto.builder()
                    .totalQuantity(user.getPoints())
                    .histories(pointDao.findByLoginId(loginId)
                                        .stream()
                                        .map(v -> PointHistoryResponseDto.builder()
                                                                        .quantity(v.getQuantity())
                                                                        .reason(v.getReason())
                                                                        .build())
                                        .collect(Collectors.toList())).build();
    }
}
