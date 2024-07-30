package com.flab.just_10_minutes.User.infrastructure;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.Util.Exception.Database.DuplicatedKeyException;
import com.flab.just_10_minutes.Util.Exception.Database.InternalException;
import com.flab.just_10_minutes.Util.Exception.Database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.Util.Exception.Database.DuplicatedKeyException.DUPLICATED_KEY_MSG;
import static com.flab.just_10_minutes.Util.Exception.Database.NotFoundException.NOT_FOUND_MSG;

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
            throw new DuplicatedKeyException(DUPLICATED_KEY_MSG);
        }
    }

    public boolean existsByLoginId(final String loginId) {
        return userMapper.existsByLoginId(loginId);
    }

    public User fetch(final String loginId) {
        return Optional.ofNullable(userMapper.findByLoginId(loginId)).orElseThrow(() -> {throw new NotFoundException(NOT_FOUND_MSG);});
    }

    public void patchPoints(final String loginId, final Long updatePoints) {
        int updateCount = userMapper.updatePoint(loginId, updatePoints);

        if (updateCount != 1) {
            throw new InternalException("Fail to Update");
        }
    }
}
