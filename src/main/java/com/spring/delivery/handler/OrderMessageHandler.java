package com.spring.delivery.handler;

import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderMessageHandler {
    private final OrderService orderService;

    @MessageMapping("/order")
    @SendTo("/topic/orders")
    public OrderDTO sendOrder(OrderDTO orderDTO){
        //  orderService.save(orderDTO);

        return orderDTO;
    }
}
