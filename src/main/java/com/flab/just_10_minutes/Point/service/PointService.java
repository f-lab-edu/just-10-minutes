package com.flab.just_10_minutes.Point.service;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointHistoryResponse;
import com.flab.just_10_minutes.Point.dto.PointHistories;
import com.flab.just_10_minutes.Point.infrastructure.repository.PointDao;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.repository.UserDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserDao userDao;
    private final PointDao pointDao;

    public PointHistory offerPoint(PointHistory pointHistory) {
        User user = userDao.fetch(pointHistory.getLoginId());

        PointHistory newHistory = pointHistory.increase(user.getPoint());
        pointDao.save(newHistory);

        userDao.patchPoint(user.getLoginId(), newHistory.getTotalQuantity());

        return pointDao.fetchFirst(pointHistory.getLoginId());
    }

    public PointHistory subtractPoint(PointHistory pointHistory) {
        if (pointHistory.isRequestZero()) {
            return pointHistory;
        }

        User user = userDao.fetch(pointHistory.getLoginId());
        if (user.getPoint() <= 0L) {throw new BusinessException("Remaining Point Under 0");}

        PointHistory newHistory = pointHistory.decrease(user.getPoint());
        pointDao.save(newHistory);
        userDao.patchPoint(user.getLoginId(), newHistory.getTotalQuantity());

        return  pointDao.fetchFirst(user.getLoginId());
    }

    public Long getTotalPoint(final String loginId) {
        User user = userDao.fetch(loginId);

        return user.getPoint();
    }

    public PointHistories getPointHistories(final String loginId) {
        User user = userDao.fetch(loginId);

        return PointHistories.from(user.getPoint(), pointDao.findByLoginId(loginId)
                                                            .stream()
                                                            .map(v -> PointHistoryResponse.from(v))
                                                            .collect(Collectors.toList()));
    }
}
