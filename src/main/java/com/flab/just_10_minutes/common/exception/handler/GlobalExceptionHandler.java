package com.flab.just_10_minutes.common.exception.handler;

import com.flab.just_10_minutes.common.exception.business.BusinessException;
import com.flab.just_10_minutes.common.exception.database.DatabaseException;
import com.flab.just_10_minutes.common.exception.database.InternalException;
import com.flab.just_10_minutes.common.exception.iamport.IamportException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
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

    @ExceptionHandler({InternalException.class})
    public ResponseEntity<ErrorResponse> handleException(final InternalException exception) {
        log.warn("InternalException occur : ", exception);

        return makeResponseEntity("Unknown Error Occur", exception.getHttpStatus());
    }

    @ExceptionHandler({DatabaseException.class})
    public ResponseEntity<ErrorResponse> handleException(final DatabaseException exception) {
        log.warn("DatabaseException occur : ", exception);

        return makeResponseEntity(exception.getMessage(), exception.getHttpStatus());
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ErrorResponse> handleException(final BusinessException exception) {
        log.warn("BusinessException occur : ", exception);

        return makeResponseEntity(exception.getMessage(), exception.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.warn("Exception occur : ", exception);
        return makeResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IamportException.class})
    public ResponseEntity<ErrorResponse> handleException(final IamportException exception) {
        log.warn("IamportException occur : ", exception.getCause());
        return makeResponseEntity(exception.getMessage(), exception.getHttpStatus());
    }

    private ResponseEntity<ErrorResponse> makeResponseEntity(final String message, final HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(httpStatus.toString(), message));
    }

    @Getter
    @RequiredArgsConstructor
    class ErrorResponse {

        private final String code;
        private final String message;
    }
}
