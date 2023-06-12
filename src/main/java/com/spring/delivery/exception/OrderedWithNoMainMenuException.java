package com.spring.delivery.exception;

public class OrderedWithNoMainMenuException extends RuntimeException{
    public OrderedWithNoMainMenuException(){
        super();
    }
    public OrderedWithNoMainMenuException(String message){
        super(message);
    }
    public OrderedWithNoMainMenuException(String message, Throwable cause){
        super(message, cause);
    }
}
