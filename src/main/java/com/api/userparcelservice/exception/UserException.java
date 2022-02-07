package com.api.userparcelservice.exception;

public class UserException extends RuntimeException {

    private String code;

    public UserException() {
        super();
    }

    public UserException(String code,
            String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
