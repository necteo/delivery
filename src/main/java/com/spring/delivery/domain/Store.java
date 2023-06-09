package com.spring.delivery.domain;

import com.spring.delivery.domain.discount.DiscountPolicy;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Address address;
    private int number;
    private String runTime;

    @Transient
    private DiscountPolicy discountPolicy;

    @OneToMany(mappedBy="store", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "STORE_ID")
    private User user;
}
