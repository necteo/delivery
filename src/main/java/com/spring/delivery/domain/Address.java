package com.spring.delivery.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zip) {
    }

    public Address() {

    }
}
