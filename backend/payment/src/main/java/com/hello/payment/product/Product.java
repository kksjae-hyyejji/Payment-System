package com.hello.payment.product;

import com.hello.payment.order.Order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name="product")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name="product_name")
    private String productName;

    @Column(name="price")
    private int price;

    @Column(name="amount")
    private int amount;

    @OneToMany(mappedBy = "product")
    private List<Order> orderList;

    @Builder
    public Product(String productName, int price, int amount, List<Order> orderList) {
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.orderList = orderList;
    }
}
