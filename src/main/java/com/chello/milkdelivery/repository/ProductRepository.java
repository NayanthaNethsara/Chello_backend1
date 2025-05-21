package com.chello.milkdelivery.repository;



import com.chello.milkdelivery.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStockLessThanEqual(int threshold);
} 
