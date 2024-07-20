package com.flab.just_10_minutes.Util.Exception;

import com.flab.just_10_minutes.Util.ErrorResult.UserErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException{

    private final UserErrorResult errorResult;
}
