package com.spring.delivery.domain.discount;

public class PercentageDiscount implements DiscountPolicy{
    private static final double DISCOUNT_RATE = 0.1;
    private static final int MIN_ORDER_AMOUNT = 15000;

    @Override
    public int calculateDiscount(int price) {
        if (price >= MIN_ORDER_AMOUNT) {
            return (int) (price - (price * DISCOUNT_RATE));
        }
        return price;
    }
}
