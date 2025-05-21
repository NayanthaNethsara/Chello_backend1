package com.chello.milkdelivery.dto;
import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private Long customerId;
    private String message;
}