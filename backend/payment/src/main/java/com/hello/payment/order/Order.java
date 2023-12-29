package com.hello.payment.order;

import com.hello.payment.product.Product;
import com.hello.payment.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "order_table")
@Getter
@NoArgsConstructor
public class Order {


    @Id
    @Column(name="order_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderSeq;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="order_price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_name")
    private Product product;

    @Builder

    public Order(Long orderSeq, User user, int price, Product product) {
        this.orderSeq = orderSeq;
        this.user = user;
        this.price = price;
        this.product = product;
    }
}
