package com.flab.just_10_minutes.User.dto;

import com.flab.just_10_minutes.Point.domain.Point;
import com.flab.just_10_minutes.User.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoDto {

    private String loginId;
    private String phone;
    private String address;
    private User.ROLE role;
    private Point point;
}
