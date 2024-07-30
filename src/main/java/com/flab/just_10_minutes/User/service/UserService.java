package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
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
