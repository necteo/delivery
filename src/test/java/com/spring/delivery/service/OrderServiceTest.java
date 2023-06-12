package com.spring.delivery.service;

import com.spring.delivery.domain.*;
import com.spring.delivery.dto.MenuRegisterDTO;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.OrderItemDTO;
import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.oauth.entity.ProviderType;
import com.spring.delivery.repository.MenuRepository;
import com.spring.delivery.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;

    @Test
    @Transactional
    @Order(1)
    void createNotTime() {
        customerBefore();
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(1L)
                .state(OrderStatus.ORDER.name())
                .orderItem(itemDTOList)
                .totalPrice(9000)
                .storeId(1L)
                .currentHour(8)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(messageForm.getMessage())
                .as("가게 운영시간이 9~18시 이므로 가게 운영 시간이 아닙니다.")
                .isEqualTo("가게 운영 시간이 아닙니다.");
    }

    @Test
    @Transactional
    @Order(2)
    void createNoMain() {
        customerBefore();
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(new OrderItemDTO(menuService.findMenuInfo("콜라").getId(), 3));
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(2L)
                .orderItem(itemDTOList)
                .totalPrice(9000)
                .storeId(1L)
                .currentHour(12)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(messageForm.getMessage())
                .as("주 메뉴가 없으면 주문이 불가능")
                .isEqualTo("사이드 메뉴 만으로는 주문이 불가능합니다.");
    }

    @Test
    @Transactional
    @Order(3)
    void createLessPrice() {
        customerBefore();
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(3L)
                .orderItem(itemDTOList)
                .totalPrice(5000)
                .storeId(1L)
                .currentHour(12)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(messageForm.getMessage())
                .as("주문 금액이 6000원 미만이어야 함")
                .isEqualTo("최소 주문 금액 6000원을 넘어야 주문이 가능합니다.");
    }

    @Test
    @Order(4)
    void create() {
        customerBefore();
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(new OrderItemDTO(menuService.findMenuInfo("싸이플렉스버거").getId(), 2));
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(4L)
                .orderItem(itemDTOList)
                .totalPrice(14000)
                .storeId(1L)
                .currentHour(12)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(orderService.findAllOrdersByUserId(messageForm.getUserId()).get(0).getOrderId())
                .as("주문의 최초 상태는 주문이어야 함")
                .isEqualTo(1L);
    }

    @Test
    @Order(5)
    void acceptOrder() {
        managerBefore();

        assertThat(orderService.findAllOrdersByUserId(4L).get(0).getState())
                .as("점주는 상태가 “주문”인 주문에 대해 접수가 가능하고 상태가 “배달중”으로 변경되는가?")
                .isEqualTo("주문");

        SocketMessageForm messageForm =
                orderService.acceptOrder(OrderDTO.builder().orderId(1L).userId(4L).build());

        assertThat(orderService.findAllOrdersByUserId(messageForm.getUserId()).get(0).getState())
                .as("점주는 상태가 “주문”인 주문에 대해 접수가 가능하고 상태가 “배달중”으로 변경되는가?")
                .isEqualTo("배달중");
    }

    @Test
    @Order(6)
    void cancel() {
//        customerBefore();
        SocketMessageForm messageForm =
                orderService.cancel(OrderDTO.builder().orderId(1L).userId(4L).build());

        assertThat(messageForm.getMessage())
                .as("상태가 “배달중”인 주문에 대해 취소가 불가한가?")
                .isEqualTo("배달중인 주문은 취소가 불가능합니다.");
    }

    @Test
    @Transactional
    @Order(7)
    void setOrderDelivered() {
//        managerBefore();
        SocketMessageForm messageForm =
                orderService.setOrderDelivered(OrderDTO.builder().orderId(1L).userId(4L).build());

        assertThat(orderService.findAllOrdersByUserId(messageForm.getUserId()).get(0).getState())
                .as("점주가 배달완료를 수행하면 해당 주문의 상태가 “완료”로 변경되는가?")
                .isEqualTo("완료");
    }

    @Test
    @Order(8)
    void findAllOrdersByUserId() {
        customerBefore();
    }

    @Test
    @Transactional
    @Order(9)
    void denyOrder() {
        managerBefore();
        orderService.init();
    }

    void customerBefore() {
        User user = User.builder()
                .username("kim")
                .email("email")
                .roleType(RoleType.CUSTOMER)
                .emailVerifiedYn("Y")
                .providerType(ProviderType.GOOGLE)
                .build();
        userService.register(user);
        MenuRegisterDTO menuRegisterDTO = new MenuRegisterDTO(
                "콜라",
                MenuType.SIDE.name().toUpperCase(),
                1000,
                "코카콜라",
                "image001",
                1L
        );
        menuService.create(menuRegisterDTO);
        menuRegisterDTO = new MenuRegisterDTO(
                "감자튀김",
                MenuType.SIDE.name().toUpperCase(),
                500,
                "프렌치 프라이",
                "image002",
                1L
        );
        menuService.create(menuRegisterDTO);
        menuRegisterDTO = new MenuRegisterDTO(
                "싸이플렉스버거",
                MenuType.MAIN.name().toLowerCase(),
                7000,
                "이 햄버거는 무척 맛있다",
                "image001",
                1L
        );
        menuService.create(menuRegisterDTO);
    }

    void managerBefore() {
        User user = User.builder()
                .username("kim")
                .email("email2")
                .roleType(RoleType.MANAGER)
                .emailVerifiedYn("Y")
                .providerType(ProviderType.GOOGLE)
                .build();
        userService.register(user);
    }

}