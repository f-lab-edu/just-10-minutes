package com.flab.just_10_minutes.user.infrastructure.repository;

import com.flab.just_10_minutes.user.domain.User;
import com.flab.just_10_minutes.user.infrastructure.entity.UserEntity;
import com.flab.just_10_minutes.util.exception.database.DuplicatedKeyException;
import com.flab.just_10_minutes.util.exception.database.InternalException;
import com.flab.just_10_minutes.util.exception.database.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.flab.just_10_minutes.util.exception.database.DuplicatedKeyException.DUPLICATED_KEY;
import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_INSERT;
import static com.flab.just_10_minutes.util.exception.database.InternalException.FAIL_TO_UPDATE;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.NOT_FOUND;
import static com.flab.just_10_minutes.util.exception.database.NotFoundException.USER;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final UserMapper userMapper;

    public Optional<UserEntity> findByLoginId(final String loginId) {
        return Optional.ofNullable(userMapper.findByLoginId(loginId));
    }

    public void save(final User user) {
        try {
            int insertCount = userMapper.save(UserEntity.from(user));

            if (insertCount != 1) {
                throw new InternalException(FAIL_TO_INSERT);
            }
        } catch (DuplicateKeyException e) {
            throw new DuplicatedKeyException(DUPLICATED_KEY);
        }
    }

    public boolean existsByLoginId(final String loginId) {
        return userMapper.existsByLoginId(loginId);
    }

    public User fetch(final String loginId) {
        return UserEntity.toDomain(Optional.ofNullable(userMapper.findByLoginId(loginId)).orElseThrow(() -> {
            throw new NotFoundException(NOT_FOUND, USER);
        }));
    }

    public void patchPoint(final String loginId, final Long updatePoints) {
        int updateCount = userMapper.updatePoint(loginId, updatePoints);

        if (updateCount != 1) {
            throw new InternalException(FAIL_TO_UPDATE);
        }
    }
}
