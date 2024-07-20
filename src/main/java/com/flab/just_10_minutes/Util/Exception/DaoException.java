package com.flab.just_10_minutes.Util.Exception;

import com.flab.just_10_minutes.Util.ErrorResult.DaoErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DaoException extends RuntimeException{

    private final DaoErrorResult errorResult;
}
