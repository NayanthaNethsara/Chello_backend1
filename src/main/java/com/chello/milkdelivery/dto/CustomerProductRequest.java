package com.chello.milkdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerProductRequest {
    private Long productId;
    private Integer quantity;
    private String deliveryDay;
    private Double amount;
}