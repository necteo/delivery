package com.spring.delivery.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;
    private String state;
    private List<OrderItemDTO> orderItem;
    private int totalPrice;
    private Long storeId;
    private int currentHour;

    @Builder
    public OrderDTO(Long orderId, Long userId, String state, List<OrderItemDTO> orderItem, int totalPrice, Long storeId, int currentHour) {
        this.orderId = orderId;
        this.userId = userId;
        this.state = state;
        this.orderItem = orderItem;
        this.totalPrice = totalPrice;
        this.storeId = storeId;
        this.currentHour = currentHour;
    }
}