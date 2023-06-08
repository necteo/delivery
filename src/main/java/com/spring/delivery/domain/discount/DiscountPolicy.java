package com.spring.delivery.domain.discount;

public interface DiscountPolicy {
    int calculateDiscount(int price);
}