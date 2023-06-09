package com.spring.delivery.repository;

import com.spring.delivery.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Order, Long> {

}
