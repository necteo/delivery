package com.spring.delivery.service;

import com.spring.delivery.domain.*;
import com.spring.delivery.dto.MenuRegisterDTO;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.OrderItemDTO;
import com.spring.delivery.dto.SocketMessageForm;
import com.spring.delivery.oauth.entity.ProviderType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("운영 시간 예외 처리")
    @Transactional
    @Order(1)
    void createNotTime() {
        Long userId = customerBefore("email");
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .state(OrderStatus.ORDER.name())
                .orderItem(itemDTOList)
                .totalPrice(9000)
                .storeId(1L)
                .currentHour(8)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(messageForm.getMessage())
                .as("운영 시간이 아닌 시점에서 장바구니 담기 예외가 발생해야함")
                .isEqualTo("가게 운영 시간이 아닙니다.");
    }

    @Test
    @DisplayName("주메뉴 없는 주문 예외 처리")
    @Transactional
    @Order(2)
    void createNoMain() {
        Long userId = customerBefore("email");
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(new OrderItemDTO(menuService.findMenuInfo("콜라").getId(), 3));
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .orderItem(itemDTOList)
                .totalPrice(9000)
                .storeId(1L)
                .currentHour(12)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(messageForm.getMessage())
                .as("장바구니에 주메뉴가 없는 상태에서 주문을 시도할 경우 예외가 발생해야함")
                .isEqualTo("사이드 메뉴 만으로는 주문이 불가능합니다.");
    }

    @Test
    @DisplayName("주문 총 가격 미달 예외 처리")
    @Transactional
    @Order(3)
    void createLessPrice() {
        Long userId = customerBefore("email");
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .orderItem(itemDTOList)
                .totalPrice(5000)
                .storeId(1L)
                .currentHour(12)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        assertThat(messageForm.getMessage())
                .as("주문 총 가격이 6000원 미만일 경우 예외가 발생해야함")
                .isEqualTo("최소 주문 금액 6000원을 넘어야 주문이 가능합니다.");
    }

    @Test
    @DisplayName("주문 최초 상태 확인")
    @Order(4)
    void create() {
        Long userId = customerBefore("email");
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(new OrderItemDTO(menuService.findMenuInfo("싸이플렉스버거").getId(), 2));
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
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
    @DisplayName("주문 상태에서 배달중 상태로")
    @Order(5)
    void acceptOrder() {
        assertThat(orderService.findAllOrdersByUserId(4L).get(0).getState())
                .as("점주는 상태가 “주문”인 주문에 대해 접수가 가능하고 상태가 “배달중”으로 변경되어야 함")
                .isEqualTo("주문");

        SocketMessageForm messageForm =
                orderService.acceptOrder(OrderDTO.builder().orderId(1L).userId(4L).build());

        assertThat(orderService.findAllOrdersByUserId(messageForm.getUserId()).get(0).getState())
                .as("점주는 상태가 “주문”인 주문에 대해 접수가 가능하고 상태가 “배달중”으로 변경되어야 함")
                .isEqualTo("배달중");
    }

    @Test
    @DisplayName("배달중 상태 주문 취소 예외 처리")
    @Order(6)
    void cancel() {
        SocketMessageForm messageForm =
                orderService.cancel(OrderDTO.builder().orderId(1L).userId(4L).build());

        assertThat(messageForm.getMessage())
                .as("상태가 “배달중”인 주문에 대해 취소가 불가해야함")
                .isEqualTo("배달중인 주문은 취소가 불가능합니다.");
    }

    @Test
    @DisplayName("배달완료시 완료 상태로")
    @Transactional
    @Order(7)
    void setOrderDelivered() {
        SocketMessageForm messageForm =
                orderService.setOrderDelivered(OrderDTO.builder().orderId(1L).userId(4L).build());

        assertThat(orderService.findAllOrdersByUserId(messageForm.getUserId()).get(0).getState())
                .as("점주가 배달완료를 수행하면 해당 주문의 상태가 “완료”로 변경되어야 함")
                .isEqualTo("완료");
    }

    @Test
    @Disabled
    @DisplayName("주문 금액 할인 정책 적용 확인")
    @Transactional
    @Order(8)
    void findAllOrdersByUserId() {
        Long userId = customerBefore("email3");
    }

    @Test
    @Disabled
    @DisplayName("1분간 미접수 시 자동 취소 확인")
    @Transactional
    @Order(9)
    void denyOrder() throws Exception {
        Long userId = customerBefore("email4");
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(new OrderItemDTO(menuService.findMenuInfo("싸이플렉스버거").getId(), 2));
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .orderItem(itemDTOList)
                .totalPrice(14000)
                .storeId(1L)
                .currentHour(12)
                .build();
        SocketMessageForm messageForm = orderService.create(orderDTO);

        sleep(61000);

        assertThat(orderService.findAllOrdersByUserId(userId).get(0).getState())
                .as("주문 이후 1분 안에 점주가 접수하지 않으면 해당 주문은 자동 취소되어야 함")
                .isEqualTo("취소");
    }

    Long customerBefore(String email) {
        User user = User.builder()
                .username("kim")
                .email(email)
                .roleType(RoleType.CUSTOMER)
                .emailVerifiedYn("Y")
                .providerType(ProviderType.GOOGLE)
                .build();
        Long userId = userService.register(user).getId();
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
        return userId;
    }
}