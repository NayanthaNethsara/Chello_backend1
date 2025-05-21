package com.chello.milkdelivery.service;

import com.chello.milkdelivery.dto.AuthenticationRequest;
import com.chello.milkdelivery.dto.AuthenticationResponse;
import com.chello.milkdelivery.dto.RegisterRequest;
import com.chello.milkdelivery.model.User;
import com.chello.milkdelivery.dto.UpdateUserDetailsRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chello.milkdelivery.config.JwtService;
import com.chello.milkdelivery.repository.UserRepository;
import com.chello.milkdelivery.dto.ForgotPasswordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var username = request.getUsername();
    var email = request.getEmail();

    var role = email.toLowerCase().endsWith("@chello.com") ? User.Role.ADMIN : User.Role.USER;

    var user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .address(request.getAddress())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .phoneNumber(request.getPhoneNumber())
            .build();

    repository.save(user);

    var accessToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user); // Generate refresh token

    return AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  public AuthenticationResponse refreshToken(String refreshToken) {
    String username = jwtService.extractUsername(refreshToken);
    var user = repository.findByUsername(username) // Retrieve user from DB
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!jwtService.validateToken(refreshToken, user)) {
      throw new RuntimeException("Invalid refresh token");
    }

    var newAccessToken = jwtService.generateToken(user);
    var newRefreshToken = jwtService.generateRefreshToken(user);

    return AuthenticationResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
  }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()));
    var user = repository.findByUsername(request.getUsername())
            .orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user); // Generate refresh token


    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  //method to check whether the user exists
  public boolean userExists(Long id, String username, User.Role role) {
    return repository.existsByIdAndUsernameAndRole(id, username, role);
  }

  //method to update password
  public void updatePassword(String username, String password) {
    var user = repository.findByUsername(username);
    user.ifPresent(u -> {
      u.setPassword(passwordEncoder.encode(password));
      repository.save(u);
    });
  }

  public boolean updateUserDetails(UpdateUserDetailsRequest request) {
    User user = repository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Update fields if they are not null or empty
    if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
      user.setPhoneNumber(request.getPhoneNumber());
    }

    if (request.getAddress() != null && !request.getAddress().isEmpty()) {
      user.setAddress(request.getAddress());
    }

    repository.save(user);
    return true;
  }
  // New forgot-password method
  public void handleForgotPassword(ForgotPasswordRequest request) {
    // Validate input
    if (request.getEmail() == null || request.getEmail().isEmpty()) {
      throw new RuntimeException("Email is required");
    }
    if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
      throw new RuntimeException("New password is required");
    }

    // Find user by email
    User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

    // Encrypt and update password
    String encryptedPassword = passwordEncoder.encode(request.getNewPassword());
    user.setPassword(encryptedPassword);

    // Save to database
    repository.save(user);
  }


}