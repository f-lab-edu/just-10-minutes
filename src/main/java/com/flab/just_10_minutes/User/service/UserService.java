package com.flab.just_10_minutes.User.service;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.persistence.UserDao;
import com.flab.just_10_minutes.Util.ErrorResult.UserErrorResult;
import com.flab.just_10_minutes.Util.Exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void save(final User user) {
        if (validateExistedUser(user.getLoginId())) {
            throw new UserException(UserErrorResult.DUPLICATED_USER_REGISTER);
        }
        userDao.save(user);
    }

    public Boolean validateExistedUser(final String loginId) {
        return userDao.findByLoginId(loginId).isPresent();
    }
}
