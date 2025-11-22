package com.emsi.conge_service.exception;

public class EmployeNotFoundException extends RuntimeException {

    public EmployeNotFoundException(String message) {
        super(message);
    }
}
