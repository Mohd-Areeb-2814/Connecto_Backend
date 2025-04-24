package com.fb.posts.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class PostsGlobalExceptionHandler {

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

    @ExceptionHandler(PostsException.class)
    public ResponseEntity<ErrorInfo> facebookExceptionHandler(PostsException exception) {

        ErrorInfo error = new ErrorInfo();

        error.setErrorMessage(environment.getProperty(exception.getMessage()));

        error.setTimestamp(LocalDateTime.now());

        error.setErrorCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<ErrorInfo>(error, HttpStatus.NOT_FOUND);

    }
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
//
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//
//    }


}