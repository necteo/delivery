package com.spring.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.delivery.domain.MenuType;
import com.spring.delivery.domain.RoleType;
import com.spring.delivery.domain.User;
import com.spring.delivery.dto.MenuRegisterDTO;
import com.spring.delivery.dto.OrderDTO;
import com.spring.delivery.dto.OrderItemDTO;
import com.spring.delivery.oauth.entity.ProviderType;
import com.spring.delivery.service.MenuService;
import com.spring.delivery.service.OrderService;
import com.spring.delivery.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AllArgsConstructor
@ComponentScan
@WebMvcTest(OrderControllerTest.class)
@Slf4j
class OrderControllerTest {
    MockMvc mockMvc;
    UserService userService;
    MenuService menuService;
    @MockBean
    OrderService orderService;

    @Test
    @WithMockUser
    void denyOrder() throws Exception {
        User user = User.builder()
                .username("kim")
                .email("email")
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
        List<OrderItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(new OrderItemDTO(menuService.findMenuInfo("싸이플렉스버거").getId(), 2));
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .orderItem(itemDTOList)
                .totalPrice(14000)
                .storeId(1L)
                .currentHour(12)
                .build();

        given(orderService.create(orderDTO).getMessage()).willReturn("주문 접수가 완료되었습니다.");
        String content = new ObjectMapper().writeValueAsString(orderDTO);

        ResultActions resultActions = mockMvc.perform(
                post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .with(csrf())
        );
        mockMvc.perform(
                get("httpstat.us/200?sleep=70000")
        );

        assertThat(orderService.findAllOrdersByUserId(userId).get(0).getState())
                .as("주문 이후 1분 안에 점주가 접수하지 않으면 해당 주문은 자동 취소되어야 함")
                .isEqualTo("취소");
    }
}