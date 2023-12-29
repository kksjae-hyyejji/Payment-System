package com.hello.payment.order.layered;

import com.hello.payment.order.Order;
import com.hello.payment.product.Product;
import com.hello.payment.product.layered.ProductRepository;
import com.hello.payment.user.layered.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hello.payment.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public String createOrder(OrderRequestDTO orderRequestDTO) {
        int priceSum=checkBuy(orderRequestDTO.getUserName(),orderRequestDTO.getProducts());
        if(priceSum==-1) return "fail";

        User user=userRepository.findByUserName(orderRequestDTO.getUserName()).orElseThrow();
        User newUser=User.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .money(user.getMoney()-priceSum)
                        .build();
        userRepository.save(newUser);

        List<Product> productList=new ArrayList<>();
        List<Order> orderList=new ArrayList<>();
        for(String productName: orderRequestDTO.getProducts()){
            Product product=productRepository.findByProductName(productName);

            Product newProduct=Product.builder()
                    .productName(productName)
                    .price(product.getPrice())
                    .amount(product.getAmount()-1)
                    .build();

            productList.add(newProduct);
            orderList.add(Order.builder()
                    .user(user)
                    .price(product.getPrice())
                    .product(newProduct)
                    .build());
        }

        productRepository.saveAll(productList);
        orderRepository.saveAll(orderList);
        return "success";
    }

    public int checkBuy(String userName, List<String> products){
        int priceSum=0;

        for(String productName:products){
            Product product=productRepository.findByProductName(productName);
            if(product.getAmount()<=0) return -1;

            priceSum+=product.getPrice();
        }

        Optional<User> user=userRepository.findByUserName(userName);
        if(user.isEmpty()||user.get().getMoney()<priceSum) return -1;

        return priceSum;
    }
}
