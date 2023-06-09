package com.spring.delivery.controller;
import com.spring.delivery.domain.Order;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public void createOrder(@RequestBody OrderDTO orderDTO){
        //orderService.create(orderDTO);
    }

    @PostMapping("/cancel")
    public void cancelOrder(@RequestParam Long orderId){
        //orderService.delete(orderId);
    }

    @GetMapping("/history")
    public void findOrderHistory(@RequestParam(required = false) Long orderId){

    }

    @PostMapping("/accept")
    public void acceptOrder(@RequestBody OrderDTO orderDTO){

    }

    @PostMapping("/delivery/complete")
    public void completeDelivery(@RequestParam OrderDTO orderDTO){

    }

    @GetMapping("/runtime")
    public void getRuntime(){

    }
}
