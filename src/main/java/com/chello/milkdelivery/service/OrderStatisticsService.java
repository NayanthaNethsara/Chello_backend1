package com.chello.milkdelivery.service;

import com.chello.milkdelivery.model.Order;
import com.chello.milkdelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderStatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    //  method for general stats
    public OrderStatistics getStatistics(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        long completedOrders = orders.stream()
                .filter(order -> order.getStatus() == Order.Status.Delivered)
                .count();

        double totalRevenue = orders.stream()
                .filter(order -> order.getStatus() == Order.Status.Delivered)
                .mapToDouble(Order::getTotal)
                .sum();

        double avgDeliveryTime = orders.stream()
                .filter(order -> order.getStatus() == Order.Status.Delivered && order.getDeliveredDate() != null)
                .mapToLong(order -> ChronoUnit.HOURS.between(
                        order.getOrderDate().atStartOfDay(), order.getDeliveredDate()))
                .average()
                .orElse(0.0);

        return new OrderStatistics(completedOrders, totalRevenue, avgDeliveryTime);
    }

    //  method for status-based stats
    public DeliveryStatusStatistics getDeliveryStatusStatistics(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        Map<String, Long> statusCounts = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getStatus().toString(),
                        Collectors.counting()
                ));

        return new DeliveryStatusStatistics(
                statusCounts.getOrDefault("Pending", 0L),
                statusCounts.getOrDefault("Processing", 0L),
                statusCounts.getOrDefault("Shipped", 0L),
                statusCounts.getOrDefault("Delivered", 0L),
                statusCounts.getOrDefault("Canceled", 0L)
        );
    }

    public static class OrderStatistics {
        private final long completedOrders;
        private final double totalRevenue;
        private final double avgDeliveryTime;

        public OrderStatistics(long completedOrders, double totalRevenue, double avgDeliveryTime) {
            this.completedOrders = completedOrders;
            this.totalRevenue = totalRevenue;
            this.avgDeliveryTime = avgDeliveryTime;
        }

        public long getCompletedOrders() { return completedOrders; }
        public double getTotalRevenue() { return totalRevenue; }
        public double getAvgDeliveryTime() { return avgDeliveryTime; }
    }

    public static class DeliveryStatusStatistics {
        private final long pending;
        private final long processing;
        private final long shipped;
        private final long delivered;
        private final long canceled;

        public DeliveryStatusStatistics(long pending, long processing, long shipped, long delivered, long canceled) {
            this.pending = pending;
            this.processing = processing;
            this.shipped = shipped;
            this.delivered = delivered;
            this.canceled = canceled;
        }

        public long getPending() { return pending; }
        public long getProcessing() { return processing; }
        public long getShipped() { return shipped; }
        public long getDelivered() { return delivered; }
        public long getCanceled() { return canceled; }
    }
}