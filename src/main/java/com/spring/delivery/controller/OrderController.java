package com.spring.delivery.controller;
import com.spring.delivery.domain.Order;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.OrderListDTO;
import com.spring.delivery.exception.StoreClosedException;
import com.spring.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/cus/order/list")
    public ResponseEntity<List<OrderDTO>> findOrderHistory(@RequestParam Long userId){
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/order/list")
    public ResponseEntity<List<OrderDTO>> findAllOrders(){
        List<OrderDTO> orders = orderService.findAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
