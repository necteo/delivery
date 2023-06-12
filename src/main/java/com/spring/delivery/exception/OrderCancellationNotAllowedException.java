package com.spring.delivery.exception;

public class OrderCancellationNotAllowedException extends RuntimeException{
    public OrderCancellationNotAllowedException(){
        super();
    }
    public OrderCancellationNotAllowedException(String message){
        super(message);
    }
    public OrderCancellationNotAllowedException(String message, Throwable cause){
        super(message, cause);
    }
}
