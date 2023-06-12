package com.spring.delivery.exception;

public class MinimumOrderAmountNotMetException extends RuntimeException{
    public MinimumOrderAmountNotMetException(){
        super();
    }
    public MinimumOrderAmountNotMetException(String message){
        super(message);
    }
    public MinimumOrderAmountNotMetException(String message, Throwable cause){
        super(message, cause);
    }
}
