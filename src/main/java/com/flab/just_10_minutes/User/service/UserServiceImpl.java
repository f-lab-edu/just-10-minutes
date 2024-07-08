package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    @Override
    public void save(User user) {

        findByLoginId(user.getLoginId()).ifPresent(v -> {throw new RuntimeException("already exist loginId");});

        int insertCount = userMapper.save(user);

        if (insertCount != 1) {
            throw new RuntimeException("Fail insert. Please retry");
        }
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {

        Assert.notNull(loginId, "loginId must not be null");

        return Optional.ofNullable(userMapper.findByLoginId(loginId));
    }
}
