package com.chello.milkdelivery.controller;

import com.chello.milkdelivery.model.User;
import com.chello.milkdelivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    // Get the current user's profile
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    // Update the current user's profile
    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        User user = userService.updateUser(updatedUser);
        return ResponseEntity.ok(user);
    }
}