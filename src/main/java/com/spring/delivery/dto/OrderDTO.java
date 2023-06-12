package com.spring.delivery.dto;

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
}