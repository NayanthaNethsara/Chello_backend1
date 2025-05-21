package com.chello.milkdelivery.service;

import com.chello.milkdelivery.dto.MessageDTO;
import com.chello.milkdelivery.dto.UserDTO;
import com.chello.milkdelivery.model.Message;
import com.chello.milkdelivery.model.User;
import com.chello.milkdelivery.repository.MessageRepository;
import com.chello.milkdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPhoneNumber() != null && !updatedUser.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getAddress() != null && !updatedUser.getAddress().isEmpty()) {
            user.setAddress(updatedUser.getAddress());
        }
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findByRole(User.Role.USER);
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setAddress(user.getAddress());
            dto.setRole(user.getRole().name());
            return dto;
        }).collect(Collectors.toList());
    }

    public MessageDTO sendMessage(Long customerId, String message) {
        Message msg = Message.builder()
                .customerId(customerId)
                .message(message)
                .build();
        Message savedMessage = messageRepository.save(msg);
        return mapToMessageDTO(savedMessage);
    }

    public List<MessageDTO> getMessagesByCustomerId(Long customerId) {
        List<Message> messages = messageRepository.findByCustomerId(customerId);
        return messages.stream().map(this::mapToMessageDTO).collect(Collectors.toList());
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    private MessageDTO mapToMessageDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setCustomerId(message.getCustomerId());
        dto.setMessage(message.getMessage());
        return dto;
    }
}