package com.api.userparcelservice.exception;

public class UserException extends RuntimeException {

    private String code;

    public UserException() {
        super();
    }

    public UserException(String message,
                         String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
