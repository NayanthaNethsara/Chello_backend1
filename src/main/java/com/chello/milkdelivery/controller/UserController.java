package com.chello.milkdelivery.controller;

import com.chello.milkdelivery.dto.MessageDTO;
import com.chello.milkdelivery.dto.UserDTO;
import com.chello.milkdelivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/{customerId}/messages")
    public MessageDTO sendMessage(@PathVariable Long customerId, @RequestBody MessageRequest request) {
        return userService.sendMessage(customerId, request.getMessage());
    }

    @GetMapping("/{customerId}/messages")
    public List<MessageDTO> getMessages(@PathVariable Long customerId) {
        return userService.getMessagesByCustomerId(customerId);
    }

    @DeleteMapping("/messages/{messageId}")
    public void deleteMessage(@PathVariable Long messageId) {
        userService.deleteMessage(messageId);
    }

    // Nested DTO for request
    static class MessageRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}