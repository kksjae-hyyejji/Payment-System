package com.hello.payment.order.layered;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public String createOrder(@RequestBody OrderRequestDTO orderRequestDTO){

        return orderService.createOrder(orderRequestDTO);
    }

}
