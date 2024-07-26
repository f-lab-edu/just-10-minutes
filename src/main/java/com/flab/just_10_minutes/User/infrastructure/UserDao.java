package com.flab.just_10_minutes.User.infrastructure;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.Util.Exception.Database.DuplicatedKeyException;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.contants.ResponseMessage.*;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final UserMapper userMapper;

    public Optional<User> findByLoginId(final String loginId) {
        return Optional.ofNullable(userMapper.findByLoginId(loginId));
    }

    public void save(final User user) {
        try {
            int insertCount = userMapper.save(user);

            if (insertCount != 1) {
                throw new InternalException(FAIL_TO_INSERT);
            }
        } catch (DuplicateKeyException e) {
            throw new DuplicatedKeyException(DUPLICATED_KEY_LOGIN_ID);
        }
    }

    public boolean existsByLoginId(final String loginId) {
        return userMapper.existsByLoginId(loginId);
    }

    public User fetch(final String loginId) {
        if (!userMapper.existsByLoginId(loginId)) {
            throw new NotFoundException(NOT_EXIST_USER);
        }
        return userMapper.findByLoginId(loginId);
    }
}
