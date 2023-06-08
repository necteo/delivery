package com.spring.delivery.domain.discount;

public class QuantityDiscount implements DiscountPolicy{
    private static final int DISCOUNT_AMOUNT = 1000;
    private static final int MIN_ORDER_AMOUNT = 10000;

    @Override
    public int calculateDiscount(int price) {
        if (price >= MIN_ORDER_AMOUNT) {
            return price - DISCOUNT_AMOUNT;
        }
        return price;
    }
}
