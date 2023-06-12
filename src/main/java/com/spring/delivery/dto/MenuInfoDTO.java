package com.spring.delivery.dto;

import com.spring.delivery.domain.DiscountPolicy;
import com.spring.delivery.domain.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuInfoDTO {
    private Long id;
    private String name;
    private String menuType;
    private String discountPolicy;
    private int price;
    private String description;
    private String imageName;
}
