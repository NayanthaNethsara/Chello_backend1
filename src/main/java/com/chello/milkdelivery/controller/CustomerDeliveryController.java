package com.chello.milkdelivery.controller;

import com.chello.milkdelivery.dto.DeliveryUpdateRequest;
import com.chello.milkdelivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerDeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/deliveries")
    public Map<String, Object> getDeliveries() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return deliveryService.getDeliveries(username);
    }

    @PutMapping("/deliveries")
    public ResponseEntity<?> updateDelivery(@RequestBody DeliveryUpdateRequest request) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        deliveryService.updateDelivery(username, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deliveries/cancel")
    public ResponseEntity<?> cancelDelivery() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        deliveryService.cancelDelivery(username);
        return ResponseEntity.ok().build();
}
}