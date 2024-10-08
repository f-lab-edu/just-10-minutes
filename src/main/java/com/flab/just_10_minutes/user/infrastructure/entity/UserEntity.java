package com.flab.just_10_minutes.user.infrastructure.entity;

import com.flab.just_10_minutes.user.domain.User;
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

    public static User toDomain(UserEntity userEntity) {
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
