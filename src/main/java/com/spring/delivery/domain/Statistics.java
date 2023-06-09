package com.spring.delivery.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;

    @OneToOne
    @JoinColumn(name = "MENU_ID")
    private Menu menu;
}
