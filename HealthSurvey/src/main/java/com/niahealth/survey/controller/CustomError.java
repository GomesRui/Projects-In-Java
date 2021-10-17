package com.niahealth.survey.controller;

import org.springframework.http.HttpStatus;

public class CustomError{
    private final HttpStatus httpStatus;
    private final String message;
    private final int code;
    
    public CustomError(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = httpStatus.value();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public String getMessage() {
        return message;
    }
    public int getCode() {
        return code;
    }

    
}
