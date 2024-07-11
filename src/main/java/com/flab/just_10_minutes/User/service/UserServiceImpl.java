package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.mapper.UserMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl{

    private final UserMapper userMapper;

    public void save(final User user) {
        if (validateExistedUser(user.getLoginId())) {
            throw new RuntimeException("already exist loginId");
        }

        int insertCount = userMapper.save(user);

        if (insertCount != 1) {
            throw new RuntimeException("Fail insert. Please retry");
        }
    }

    public Optional<User> findByLoginId(@NonNull final String loginId) {
        return Optional.ofNullable(userMapper.findByLoginId(loginId));
    }

    public Boolean validateExistedUser(final String loginId) {
        return findByLoginId(loginId).isPresent();
    }
}
