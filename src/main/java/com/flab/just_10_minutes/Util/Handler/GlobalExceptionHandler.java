package com.flab.just_10_minutes.Util.Handler;

import com.flab.just_10_minutes.Util.ErrorResult.CommonErrorResult;
import com.flab.just_10_minutes.Util.ErrorResult.UserErrorResult;
import com.flab.just_10_minutes.Util.Exception.UserException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final List<String> errorList = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("Invalid DTO Parameter errors : {}", errorList);

        return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommonErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final UserErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getHttpStatus())
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    @ExceptionHandler({UserException.class})
    public ResponseEntity<ErrorResponse> handleRestApiException(final UserException exception) {
        log.warn("UserException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getErrorResult());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.warn("Exception occur : ", exception);
        return this.makeErrorResponseEntity(CommonErrorResult.UNKNOWN_EXCEPTION);
    }

    @Getter
    @RequiredArgsConstructor
    class ErrorResponse {

        private final String code;
        private final String message;
    }
}
