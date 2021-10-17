package com.niahealth.survey.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationMessage
{
    @ExceptionHandler(HTTP_METHOD_NOT_AVAILABLE.class)
    public ResponseEntity<CustomError> handleException(HTTP_METHOD_NOT_AVAILABLE e) 
    {
        CustomError error = new CustomError(HttpStatus.SERVICE_UNAVAILABLE, e.getLocalizedMessage());
        return new ResponseEntity<CustomError>(error, error.getHttpStatus());
    }

    public static class HTTP_METHOD_NOT_AVAILABLE extends RuntimeException
    {
        public HTTP_METHOD_NOT_AVAILABLE(String name)
        {
            super("The following HTTP Method is not available: " + name);
        }
    }

    @ExceptionHandler(ENTITY_NOT_FOUND.class)
    public ResponseEntity<CustomError> handleException(ENTITY_NOT_FOUND e) 
    {
        CustomError error = new CustomError(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<CustomError>(error, error.getHttpStatus());
    }

    public static class ENTITY_NOT_FOUND extends RuntimeException
    {
        public ENTITY_NOT_FOUND(String name)
        {
            super("The following Entity cannot be found in the database: " + name);
        }
        public ENTITY_NOT_FOUND(String name, Object value)
        {
            super("The following Entity cannot be found in the database: " + name + " ["+value+"]");
        }
    }

    @ExceptionHandler(ENTITY_ALREADY_EXISTS.class)
    public ResponseEntity<CustomError> handleException(ENTITY_ALREADY_EXISTS e) 
    {
        CustomError error = new CustomError(HttpStatus.CONFLICT, e.getLocalizedMessage());
        return new ResponseEntity<CustomError>(error, error.getHttpStatus());
    }

    public static class ENTITY_ALREADY_EXISTS extends RuntimeException
    {
        public ENTITY_ALREADY_EXISTS(String name)
        {
            super("The following Entity already exists in the database: " + name);
        }
        public ENTITY_ALREADY_EXISTS(String name, Object value)
        {
            super("The following Entity already exists in the database: " + name + " ["+value+"]");
        }
    }

    @ExceptionHandler(CHECK_VALUE_FAILED.class)
    public ResponseEntity<CustomError> handleException(CHECK_VALUE_FAILED e) 
    {
        CustomError error = new CustomError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<CustomError>(error, error.getHttpStatus());
    }

    public static class CHECK_VALUE_FAILED extends RuntimeException
    {
        public CHECK_VALUE_FAILED(String name, Object value)
        {
            super("The following value has not been validated by the check constraint: " + value + "["+name+"]");
        }
    }

    @ExceptionHandler(INPUT_VALUE_NULL.class)
    public ResponseEntity<CustomError> handleException(INPUT_VALUE_NULL e) 
    {
        CustomError error = new CustomError(HttpStatus.NO_CONTENT, e.getLocalizedMessage());
        return new ResponseEntity<CustomError>(error, error.getHttpStatus());
    }

    public static class INPUT_VALUE_NULL extends RuntimeException
    {
        public INPUT_VALUE_NULL(String name)
        {
            super("The following mandatory field provided was set to null / empty: " + name);
        }
    }
}