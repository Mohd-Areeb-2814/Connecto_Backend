package com.fb.user.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserGlobalExceptionHandler {

    @Autowired
    Environment environment;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> generalExceptionHandler(Exception exception) {

        ErrorInfo error = new ErrorInfo();

        error.setErrorMessage(environment.getProperty("General.EXCEPTION_MESSAGE"));

        error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ErrorInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorInfo> badCredentialsExceptionHandler(BadCredentialsException exception) {
        ErrorInfo error = new ErrorInfo();
        error.setErrorMessage(exception.getMessage()); // Extract the message from the exception
        error.setErrorCode(HttpStatus.UNAUTHORIZED.value()); // Unauthorized HTTP status code
        error.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorInfo> facebookExceptionHandler(UserException exception) {

        ErrorInfo error = new ErrorInfo();

        error.setErrorMessage(environment.getProperty(exception.getMessage()));

        error.setTimestamp(LocalDateTime.now());

        error.setErrorCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<ErrorInfo>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorInfo> securityExceptionHandler(UsernameNotFoundException exception) {

        ErrorInfo error = new ErrorInfo();

        error.setErrorMessage(environment.getProperty(exception.getMessage()));

        error.setTimestamp(LocalDateTime.now());

        error.setErrorCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<ErrorInfo>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({RestClientException.class, HttpClientErrorException.class})
    public ResponseEntity<ErrorInfo> restClientException(RestClientException exception) {

        ErrorInfo error = new ErrorInfo();
        exception.printStackTrace();
        String exceptionMessage = exception.getMessage();
        if(! exceptionMessage.equals("Something went wrong, please check the log."))
        {
            exceptionMessage = exceptionMessage.substring(exceptionMessage.indexOf('{')+1, exceptionMessage.indexOf('}'));
            exceptionMessage = exceptionMessage.split(",")[0].split(":")[1];
            exceptionMessage = exceptionMessage.substring(exceptionMessage.indexOf('"')+1, exceptionMessage.lastIndexOf('"'));

        }
        error.setErrorMessage(exceptionMessage);
        error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, ConstraintViolationException.class })
    public ResponseEntity<ErrorInfo> exceptionHandler(Exception exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
        String errorMsg = "";
        if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception1 = (MethodArgumentNotValidException) exception;
            errorMsg = exception1.getBindingResult().getAllErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            ConstraintViolationException exception1 = (ConstraintViolationException) exception;
            errorMsg = exception1.getConstraintViolations().stream().map(x -> x.getMessage())
                    .collect(Collectors.joining(", "));
        }
        errorInfo.setErrorMessage(errorMsg);
        errorInfo.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

}
