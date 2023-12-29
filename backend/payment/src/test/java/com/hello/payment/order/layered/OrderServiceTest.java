package com.hello.payment.order.layered;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void createOrder(){
        List<String> products=new ArrayList<>();
        products.add("apple");
        products.add("banana");
        orderService.createOrder(new OrderRequestDTO("b",products));
    }

}