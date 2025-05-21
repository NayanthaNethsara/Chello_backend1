package com.chello.milkdelivery.service;

import com.chello.milkdelivery.dto.DeliveryUpdateRequest;
import com.chello.milkdelivery.model.CustomerProduct;
import com.chello.milkdelivery.model.Product;
import com.chello.milkdelivery.repository.CustomerProductRepository;
import com.chello.milkdelivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final CustomerProductRepository customerProductRepository;
    private final ProductRepository productRepository;

    public Map<String, Object> getDeliveries(String username) {
        List<CustomerProduct> wednesdayDeliveries = customerProductRepository.findDeliveriesByDay(username, "Wednesday");
        List<CustomerProduct> sundayDeliveries = customerProductRepository.findDeliveriesByDay(username, "Sunday");

        Map<String, Object> response = new HashMap<>();
        response.put("wednesday", processDeliveries(wednesdayDeliveries));
        response.put("sunday", processDeliveries(sundayDeliveries));
        response.put("wednesdayTotal", calculateTotal(wednesdayDeliveries));
        response.put("sundayTotal", calculateTotal(sundayDeliveries));

        return response;
    }

    private List<Map<String, Object>> processDeliveries(List<CustomerProduct> deliveries) {
        Map<Long, Map<String, Object>> productMap = new HashMap<>();

        for (CustomerProduct cp : deliveries) {
            Product product = productRepository.findById(cp.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + cp.getProductId()));

            productMap.computeIfAbsent(cp.getProductId(), k -> {
                Map<String, Object> item = new HashMap<>();
                item.put("name", product.getName());
                item.put("image", product.getImage() != null ? product.getImage() : "/images/placeholder.jpg");
                item.put("quantity", 0);
                item.put("totalPrice", 0.0);
                return item;
            });

            Map<String, Object> item = productMap.get(cp.getProductId());
            item.put("quantity", (Integer) item.get("quantity") + cp.getQuantity());
            item.put("totalPrice", (Double) item.get("totalPrice") + cp.getAmount());
        }

        return new ArrayList<>(productMap.values());
    }

    public void updateDelivery(String username, DeliveryUpdateRequest request) {
        List<CustomerProduct> deliveries = customerProductRepository.findAllByUsername(username);

        for (CustomerProduct cp : deliveries) {
            cp.setDeliveryMethod(request.getMethod());
            cp.setDeliveryAddress(request.getAddress());
            cp.setAvailability(request.getAvailability());
            customerProductRepository.save(cp);
        }
    }

    public void cancelDelivery(String username) {
        List<CustomerProduct> deliveries = customerProductRepository.findAllByUsername(username);
        for (CustomerProduct cp : deliveries) {
            cp.setCancelled(true);
            customerProductRepository.save(cp);
        }
    }

    private Double calculateTotal(List<CustomerProduct> deliveries) {
        return deliveries.stream()
                .mapToDouble(CustomerProduct::getAmount)
                .sum();
    }
}