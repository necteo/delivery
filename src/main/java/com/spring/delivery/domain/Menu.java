package com.spring.delivery.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private MenuType menuType;
    private DiscountPolicy discountPolicy;
    private int price;
    private String description;

    private String imageName;   //  이미지 저장 타입

    @ManyToOne
    @JoinColumn(name="STORE_ID")
    private Store store;

    @OneToOne
    private Statistics statistics;

    @OneToOne(mappedBy = "menu")
    private OrderItem orderItem;

    public Menu(String name, MenuType main, DiscountPolicy aDefault, int price, String description, String imageName,
                Store store) {
        this.name = name;
        this.menuType = main;
        this.discountPolicy = aDefault;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
        this.store = store;
    }

    public Menu(String name, MenuType main, DiscountPolicy aDefault, int price, String description, String imageName,
                Store store, Statistics statistics, OrderItem orderItem) {
        this.name = name;
        this.menuType = main;
        this.discountPolicy = aDefault;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
        this.store = store;
        this.statistics = statistics;
        this.orderItem = orderItem;
    }
}
