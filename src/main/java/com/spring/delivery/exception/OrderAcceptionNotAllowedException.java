package com.spring.delivery.exception;

public class OrderAcceptionNotAllowedException extends RuntimeException{
    public OrderAcceptionNotAllowedException(){
        super();
    }
    public OrderAcceptionNotAllowedException(String message){
        super(message);
    }
    public OrderAcceptionNotAllowedException(String message, Throwable cause){
        super(message, cause);
    }
}
