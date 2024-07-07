package com.flab.just_10_minutes.User.dto;

import com.flab.just_10_minutes.User.domain.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class MemberDto {
    @NotEmpty
    @Size(min = 5, max = 20, message = "로그인 아이디는 5자리 이상 20자리 이하여야 합니다.")
    private Long loginId;
    @NotEmpty
    @Size(min = 8, max = 20, message = "비밀번호는 8자리 이상 20자리 이하여야 합니다.")
    private String password;
    @NotEmpty
    private String phone;
    @NotEmpty
    private String address;
    private User.ROLE role;

}
