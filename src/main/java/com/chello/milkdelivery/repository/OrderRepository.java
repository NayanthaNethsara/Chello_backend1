package com.chello.milkdelivery.repository;

import com.chello.milkdelivery.model.Order;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
}