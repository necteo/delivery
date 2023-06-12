package com.spring.delivery.exception;

public class DuplicatedMenuException extends IllegalStateException {
    public DuplicatedMenuException(String message) {
        super(message);
    }

    public DuplicatedMenuException(String message, Throwable cause) {
        super(message, cause);
    }
}
