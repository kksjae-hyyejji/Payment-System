package com.hello.payment.order.layered;

import com.hello.payment.product.Product;
import com.hello.payment.product.layered.ProductRepository;
import org.assertj.core.api.Assertions;
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
    
    @Autowired
    private ProductRepository productRepository;

    @Test
    void createOrder(){
        List<String> products=new ArrayList<>();
        products.add("apple");
        products.add("banana");
        orderService.createOrder(new OrderRequestDTO("b",products));
    }
    
    @Test
    void findAllByProductName() {
        List<String> products = new ArrayList<>();
        products.add("apple");
        products.add("banana");
        products.add("grapes");
        products.add("strawberry");
        
        List<Product> list = productRepository.findAllByProductNameIn(products);

        Assertions.assertThat(list.size()).isEqualTo(4);
        
        for (Product p : list)
            System.out.println("p = " + p);
        
    }

}