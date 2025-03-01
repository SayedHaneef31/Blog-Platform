package com.Sayed.Blog.Backend.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice // Global Exception Handling: It allows you to handle exceptions across the whole application in one place.
                  //Applies to All Controllers: It acts like an "Interceptor" for handling exceptions globally.
@Slf4j            //enables Logging: It provides a log object without manually defining it.
public class ErrorController
{
    // Handling BaseException
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    // Handling IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    //Handling BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> hanndleBadCredentialsException(BadCredentialsException ex)
    {
        log.error("BadCredentialsException occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
