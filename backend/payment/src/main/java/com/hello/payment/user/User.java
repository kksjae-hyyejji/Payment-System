package com.hello.payment.user;

import com.hello.payment.order.Order;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity(name="user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="money")
    private int money;

    @OneToMany(mappedBy = "user")
    private List<Order> orderList;

    @Builder
    public User(Long userId, String userName, int money, List<Order> orderList) {
        this.userId = userId;
        this.userName = userName;
        this.money = money;
        this.orderList = orderList;
    }
}
