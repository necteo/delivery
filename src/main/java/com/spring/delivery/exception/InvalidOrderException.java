package com.spring.delivery.exception;

public class InvalidOrderException extends RuntimeException {
    private Long userId;
    private String userEmail;

    public InvalidOrderException(String message){
        super(message);
    }

    public InvalidOrderException(String message, Long userId) {
        super(message);
        this.userId = userId;
    }
    public InvalidOrderException(String message, String userEmail) {
        super(message);
        this.userEmail = userEmail;
    }

    public Long getUserId() {
        return userId;
    }
}