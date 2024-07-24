package com.flab.just_10_minutes.User.persistence;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.Util.Exception.Database.DuplicatedKeyException;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import java.util.Optional;

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
                throw new InternalException("Fail to Insert");
            }
        } catch (DuplicateKeyException e) {
            throw new DuplicatedKeyException("Duplicate User loginId for Registration");
        }
    }
}
