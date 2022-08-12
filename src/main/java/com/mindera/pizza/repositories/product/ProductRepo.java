package com.mindera.pizza.repositories.product;

import com.mindera.pizza.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
