package com.spring.delivery.repository;

import com.spring.delivery.domain.Order;
import com.spring.delivery.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository <Order, Long> {
    Optional<List<Order>> findAllByUserId(Long userId);
    Optional<List<Order>> findAllByStatus(OrderStatus status);
}
