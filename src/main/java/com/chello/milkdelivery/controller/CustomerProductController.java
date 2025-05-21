package com.chello.milkdelivery.controller;

import com.chello.milkdelivery.dto.CustomerProductRequest;
import com.chello.milkdelivery.model.CustomerProduct;
import com.chello.milkdelivery.model.Product;
import com.chello.milkdelivery.service.CustomerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-products")
public class CustomerProductController {

    @Autowired
    private CustomerProductService customerProductService;

    @PostMapping
    public ResponseEntity<?> createCustomerProduct(@RequestBody CustomerProductRequest request) {
        try {
            CustomerProduct saved = customerProductService.createCustomerProduct(request);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    @GetMapping("/favorites")
    public ResponseEntity<List<Product>> getCustomerFavorites() {
        try {
            List<Product> favorites = customerProductService.getCustomerFavoriteProducts();
            return ResponseEntity.ok(favorites);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    // Inner class for error response
    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}