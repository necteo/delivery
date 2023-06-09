package com.spring.delivery.service;

import com.spring.delivery.domain.Order;
import com.spring.delivery.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private Order create(Order order){
        return orderRepository.save(order);
    }
}
