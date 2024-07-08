package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public interface UserService {

    void save(User user);

    Optional<User> findByLoginId(final String loginId);
}
