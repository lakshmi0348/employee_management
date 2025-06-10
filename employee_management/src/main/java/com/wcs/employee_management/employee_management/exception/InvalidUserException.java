package com.wcs.employee_management.employee_management.exception;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException(String message) {
        super(message);
    }
}
