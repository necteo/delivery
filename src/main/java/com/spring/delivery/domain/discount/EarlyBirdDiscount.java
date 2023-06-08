package com.spring.delivery.domain.discount;

import java.time.LocalDateTime;

public class EarlyBirdDiscount implements DiscountPolicy{
    private static final int DISCOUNT_AMOUNT = 1000;
    private static final int MORNING_DISCOUNT_HOUR = 11;

    @Override
    public int calculateDiscount(int price) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.getHour() < MORNING_DISCOUNT_HOUR) {
            return price - DISCOUNT_AMOUNT;
        }
        return price;
    }
}
