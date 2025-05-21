package com.chello.milkdelivery.dto;


import com.chello.milkdelivery.model.Order;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public class PlaceOrderDto {
    @NotNull
    @NotEmpty
    private String customerName;

    private LocalDate orderDate; 

    @NotNull
    @NotEmpty
    private List<String> items;

    @NotNull
    @Positive
    private double total;

    // ✅ New Fields for Fulfillment Method
    @NotNull
    private Order.FulfillmentMethod fulfillmentMethod;  // "DELIVERY" or "PICKUP"

    private String pickupLocation;  // Required if PICKUP
    private String deliverySchedule;  // Required if DELIVERY

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Order.FulfillmentMethod getFulfillmentMethod() {
        return fulfillmentMethod;
    }

    public void setFulfillmentMethod(Order.FulfillmentMethod fulfillmentMethod) {
        this.fulfillmentMethod = fulfillmentMethod;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(String deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

}