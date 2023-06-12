package com.spring.delivery.handler;

import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
//
    @ExceptionHandler(value = RuntimeException.class)
    public String HandleRuntimeException(RuntimeException e){
        log.error("Error occurred: {}", e);
        return e.getMessage();
    }

    @ExceptionHandler(value = InvalidOrderException.class)
    public SocketMessageForm HandleInvalidOrderException(InvalidOrderException e){
        SocketMessageForm messageForm = new SocketMessageForm(true);
        log.error("Error occurred: {}", e);
        messageForm.setState(false);
        messageForm.setMessage(e.getMessage());
        return messageForm;
    }
//
//
//    @ExceptionHandler(value = StoreClosedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleStoreClosedException(StoreClosedException e) {
//        log.error("Error occurred: {}", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(value = MinimumOrderAmountNotMetException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleMinimumOrderAmountNotMetException(MinimumOrderAmountNotMetException e){
//        log.error("Error occurred: {}", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(value = OrderedWithNoMainMenuException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleOrderedWithNoMainMenuException(OrderedWithNoMainMenuException e){
//        log.error("Error occurred: {}", e);
//        return e.getMessage();
//    }
//
//    @ExceptionHandler(value = OrderCancellationNotAllowedException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String HandleOrderCancellationNotAllowedException(OrderCancellationNotAllowedException e){
//        log.error("Error occurred: {}", e);
//        return e.getMessage();
//    }
}
