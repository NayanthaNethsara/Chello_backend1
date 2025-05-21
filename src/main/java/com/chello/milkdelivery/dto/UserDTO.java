package com.chello.milkdelivery.dto;



import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
}