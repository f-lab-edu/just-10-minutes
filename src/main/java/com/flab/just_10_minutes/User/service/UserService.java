package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.infrastructure.UserDao;
import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.flab.just_10_minutes.Util.contants.ResponseMessage.DUPLICATED_REGISTER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void save(final User user) {
        if (existsByLoginId(user.getLoginId())) {
            throw new BusinessException(DUPLICATED_REGISTER);
        }
        userDao.save(user);
    }

    public boolean existsByLoginId(final String loginId) {
        return userDao.existsByLoginId(loginId);
    }

    public User getUserProfile(final String loginId) {
        return userDao.fetch(loginId);
    }
}
