package com.api.userparcelservice.controller;

import com.api.userparcelservice.domain.ErrorResponse;
import com.api.userparcelservice.exception.JwtAuthenticationException;
import com.api.userparcelservice.exception.UserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {


    @ExceptionHandler(UserException.class)
    public ErrorResponse handleUserException(UserException e) {

        return ErrorResponse.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build();
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ErrorResponse handleJwtAuthException(JwtAuthenticationException e) {
        return ErrorResponse.builder()
                .code("008")
                .description(e.getMessage())
                .build();
    }
}
