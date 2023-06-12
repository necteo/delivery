package com.spring.delivery.controller;

import com.spring.delivery.domain.Store;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.repository.StoreRepository;
import com.spring.delivery.service.OrderService;
import com.spring.delivery.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderControllerTest.class)
@Slf4j
class OrderControllerTest {
}