package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import java.util.Optional;

public interface UserService {

    void save(User user);

    Optional<User> findByLoginId(final String loginId);
}
