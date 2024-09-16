package com.flab.just_10_minutes.user.dto;

import com.flab.just_10_minutes.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileDto {

    private String loginId;
    private String phone;
    private String address;
    private User.ROLE role;
}
