package com.chello.milkdelivery.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_product")
public class CustomerProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "delivery_day", nullable = false, length = 20)
    private String deliveryDay;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // NEW FIELDS
    @Column(name = "delivery_method")
    private String deliveryMethod; // e.g., "delivery" or "pickup"

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "availability")
    private String availability; // Can be a time window like "8am–10am"

    @Column(name = "cancelled")
    private boolean cancelled = false;
}