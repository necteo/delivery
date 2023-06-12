package com.spring.delivery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private Long storeId = 1L;
    private Long menuId;
    private int quantity;

    public OrderItemDTO(Long menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
