package com.spring.delivery.dto;

import com.spring.delivery.domain.DiscountPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDiscountPolicyDTO {
    private String name;
    private String discountPolicy;
}
