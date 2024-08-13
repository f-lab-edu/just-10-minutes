package com.flab.just_10_minutes.Util.Handler;

import com.flab.just_10_minutes.Util.Exception.Business.BusinessException;
import com.flab.just_10_minutes.Util.Exception.Database.DatabaseException;
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
