package com.chello.milkdelivery.dto;

import com.chello.milkdelivery.model.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String username;
  private String email;
  private String password;
  private String phoneNumber;
  private String address;
  private Role role;
}