package com.hello.payment.order.layered;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDTO {

    private String userName;
    private List<String> products;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(String userName, List<String> products) {
        this.userName = userName;
        this.products = products;
    }
}
