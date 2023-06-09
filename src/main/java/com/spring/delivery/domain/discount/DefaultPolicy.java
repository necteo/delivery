package com.spring.delivery.domain.discount;

import java.time.LocalDateTime;

public class DefaultPolicy implements DiscountPolicy {
    @Override
    public int calculateDiscount(int price) {
        return price;
    }
}
