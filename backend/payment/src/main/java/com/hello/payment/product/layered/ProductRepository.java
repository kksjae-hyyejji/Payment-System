package com.hello.payment.product.layered;

import com.hello.payment.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {
    Product findByProductName(String productName);

    List<Product> findAllByProductNameIn(List<String> products);
}
