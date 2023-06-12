package com.spring.delivery.dto;

import com.spring.delivery.domain.MenuType;
import com.spring.delivery.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRegisterDTO {
    private String name;
    private String menuType;
    private int price;
    private String description;
    private String imageName;
    private Long storeId;
}
