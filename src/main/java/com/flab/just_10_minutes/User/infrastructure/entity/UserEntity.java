package com.flab.just_10_minutes.User.infrastructure.entity;

import com.flab.just_10_minutes.User.domain.User;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class UserEntity {

    private Long id;
    private String loginId;
    private String password;
    private String phone;
    private String address;
    private User.ROLE role;
    private Long point;

    public static UserEntity from(User user) {
        return UserEntity.builder()
                        .loginId(user.getLoginId())
                        .password(user.getPassword())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .role(user.getRole())
                        .point(user.getPoint())
                        .build();
    }

    public static User to(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .loginId(userEntity.getLoginId())
                .password(userEntity.getPassword())
                .phone(userEntity.getPhone())
                .address(userEntity.getAddress())
                .role(userEntity.getRole())
                .point(userEntity.getPoint())
                .build();
    }
}
