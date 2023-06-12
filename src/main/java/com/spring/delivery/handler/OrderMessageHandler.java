package com.spring.delivery.handler;

import com.spring.delivery.domain.Order;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderMessageHandler {
    private final OrderService orderService;

    @MessageMapping("/order/create")
    @SendTo("/topic/orders")
    public SocketMessageForm sendOrder(OrderDTO orderDTO){
        return orderService.create(orderDTO);
    }

    @MessageMapping("/order/cancel")
    @SendTo("/topic/orders")
    public SocketMessageForm cancelOrder(OrderDTO orderDTO){
        return orderService.cancel(orderDTO);
    }


    @MessageMapping("/order/accept")
    @SendTo("/topic/orders")
    public SocketMessageForm acceptOrder(OrderDTO orderDTO){
        return orderService.acceptOrder(orderDTO);
    }

    @MessageMapping("/order/deny")
    @SendTo("/topic/orders")
    public SocketMessageForm denyOrder(OrderDTO orderDTO){
        return orderService.denyOrder(orderDTO);
    }

    @MessageMapping("/order/complete")
    @SendTo("/topic/orders")
    public SocketMessageForm completeDelivery(OrderDTO orderDTO){
        return orderService.setOrderDelivered(orderDTO);
    }
}
