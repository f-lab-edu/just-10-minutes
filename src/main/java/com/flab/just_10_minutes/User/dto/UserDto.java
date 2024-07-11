package com.flab.just_10_minutes.User.dto;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.Util.validator.PhoneNum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserDto{

    @NotEmpty
    @Size(min = 5, max = 12, message = "로그인 아이디는 5자리 이상 12자리 이하여야 합니다.")
    private String loginId;
    @NotEmpty
    @Size(min = 8, max = 15, message = "비밀번호는 8자리 이상 15자리 이하여야 합니다.")
    private String password;
    @PhoneNum
    private String phone;
    @NotEmpty
    private String address;
    private User.ROLE role;
}
