package com.flab.just_10_minutes.Point.service;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.mapper.PointMapper;
import com.flab.just_10_minutes.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserService userService;
    private final PointMapper pointMapper;

    public void save(PointHistory pointHistory) {
        userService.update(pointHistory.getLoginId(), pointHistory.getQuantity());

        int saveResult = pointMapper.save(pointHistory);
        if (saveResult != 1) {
            throw new RuntimeException("Fail insert. Please retry");
        }
    }

    public List<PointHistory> findByLoginId(final String loginId) {
        List<PointHistory> result = pointMapper.findByLoginId(loginId);

        return result == null ? new ArrayList<>() : result;
    }
}
