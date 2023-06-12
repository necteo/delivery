package com.spring.delivery.exception;

public class StoreClosedException extends RuntimeException{
    public StoreClosedException(){
        super();
    }
    public StoreClosedException(String message){
        super(message);
    }
    public StoreClosedException(String message, Throwable cause){
        super(message, cause);
    }
}
