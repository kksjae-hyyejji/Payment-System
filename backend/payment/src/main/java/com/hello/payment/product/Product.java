package com.hello.payment.product;

import com.hello.payment.order.Order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_seq")
    private Order order;

    @Builder
    public Product(String productName, int price, int amount, Order order) {
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.order = order;
    }
}
