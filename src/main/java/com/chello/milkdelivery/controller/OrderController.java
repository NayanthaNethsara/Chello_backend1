package com.chello.milkdelivery.controller;

import com.chello.milkdelivery.dto.DriverAssignmentDto;
import com.chello.milkdelivery.dto.OrderStatusUpdateDto;
import com.chello.milkdelivery.dto.PlaceOrderDto;
import com.chello.milkdelivery.model.Order;
import com.chello.milkdelivery.repository.OrderRepository;
import com.chello.milkdelivery.service.OrderStatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@Valid @RequestBody PlaceOrderDto placeOrderDto) {
        Order order = new Order();
        order.setCustomerName(placeOrderDto.getCustomerName());
        order.setStatus(Order.Status.Pending);
        order.setDriverId(null);
        order.setOrderDate(placeOrderDto.getOrderDate() != null ? placeOrderDto.getOrderDate() : LocalDate.now());
        try {
            order.setItems(objectMapper.writeValueAsString(placeOrderDto.getItems()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize items to JSON", e);
        }
        order.setTotal(placeOrderDto.getTotal());

        order.setFulfillmentMethod(placeOrderDto.getFulfillmentMethod());//saving the method of fulfillment
        order.setPickupLocation(placeOrderDto.getPickupLocation());//saving the pickup location
        order.setDeliverySchedule(placeOrderDto.getDeliverySchedule());//saving the delivery schedule

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(201).body(savedOrder);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @Valid @RequestBody OrderStatusUpdateDto statusUpdateDto) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        try {
            Order.Status newStatus = Order.Status.valueOf(statusUpdateDto.getStatus());
            order.setStatus(newStatus);
            orderRepository.save(order);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{orderId}/assign-driver")
    public ResponseEntity<Order> assignDriver(@PathVariable Long orderId, @Valid @RequestBody DriverAssignmentDto driverAssignmentDto) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();
        order.setDriverId(driverAssignmentDto.getDriverId());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/statistics")
    public OrderStatisticsService.OrderStatistics getOrderStatistics(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return orderStatisticsService.getStatistics(start, end);
    }
    @GetMapping("/delivery-statistics")
    public OrderStatisticsService.DeliveryStatusStatistics getDeliveryStatusStatistics(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return orderStatisticsService.getDeliveryStatusStatistics(start, end);
    }
}