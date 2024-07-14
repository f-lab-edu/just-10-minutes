package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.Point.domain.Point;
import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.service.PointService;
import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFacadeService {

    private final UserService userService;
    private final PointService pointService;

    public UserInfoDto findByLoginId(final String loginId) {
        User existUser = userService.findByLoginId(loginId).orElseThrow(() -> {throw new RuntimeException("not exist user");});

        List<PointHistory> pointHistories = pointService.findByLoginId(loginId);

        Point point = Point.builder()
                .totalQuantity(existUser.getPoint())
                .histories(pointHistories)
                .build();

        return UserInfoDto.builder()
                .loginId(existUser.getLoginId())
                .phone(existUser.getPhone())
                .address(existUser.getAddress())
                .role(existUser.getRole())
                .point(point)
                .build();
    }
}
