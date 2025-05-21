package com.chello.milkdelivery.repository;

import com.chello.milkdelivery.model.CustomerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Long> {

    @Query("SELECT cp FROM CustomerProduct cp WHERE cp.username = :username AND cp.cancelled = false AND (cp.deliveryDay = 'Wednesday' OR cp.deliveryDay = 'Wednesday,Sunday')")
    List<CustomerProduct> findWednesdayDeliveries(String username);

    @Query("SELECT cp FROM CustomerProduct cp WHERE cp.username = :username AND cp.cancelled = false AND (cp.deliveryDay = 'Sunday' OR cp.deliveryDay = 'Wednesday,Sunday')")
    List<CustomerProduct> findSundayDeliveries(String username);

    @Query("SELECT DISTINCT cp.productId FROM CustomerProduct cp WHERE cp.username = :username")
    Set<Long> findDistinctProductIdsByUsername(String username);

    @Query("SELECT cp FROM CustomerProduct cp WHERE cp.username = :username AND cp.cancelled = false AND cp.deliveryDay = :day")
    List<CustomerProduct> findDeliveriesByDay(String username, String day);

    CustomerProduct findByIdAndUsername(Long id, String username);

    List<CustomerProduct> findAllByUsername(String username);
}