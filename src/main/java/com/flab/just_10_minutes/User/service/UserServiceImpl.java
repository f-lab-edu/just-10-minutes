package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
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
    public Optional<User> findByLoginId(@NonNull String loginId) {
        return Optional.ofNullable(userMapper.findByLoginId(loginId));
    }
}
