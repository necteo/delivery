package com.spring.delivery.service;

import com.spring.delivery.domain.*;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.OrderItemDTO;
import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.repository.MenuRepository;
import com.spring.delivery.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderServiceTest {
    @Test
    void create() {
    }

    @Test
    void cancel() {
    }

    @Test
    void findAllOrdersByUserId() {
    }

    @Test
    void acceptOrder() {
    }

    @Test
    void denyOrder() {
    }

    @Test
    void setOrderDelivered() {
    }
}