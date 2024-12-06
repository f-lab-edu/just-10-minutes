package com.flab.just_10_minutes.user.service;

import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.repository.UserDao;
import com.flab.just_10_minutes.common.exception.business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void save(final User user) {
        if (userDao.existsByLoginId(user.getLoginId())) {
            throw new BusinessException("Duplicate User Registration Request");
        }
        userDao.save(user);
    }

    public User getUserProfile(final String loginId) {
        return userDao.fetch(loginId);
    }
}
