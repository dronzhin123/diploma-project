package com.example.orderservice.domain.service;

import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.entity.OrderStatus;
import com.example.orderservice.domain.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Long userId, BigDecimal amount, LocalDateTime deliveryStart, LocalDateTime deliveryEnd) {
        validateWindow(deliveryStart, deliveryEnd);

        Order order = Order.builder()
                .userId(userId)
                .amount(amount)
                .status(OrderStatus.PENDING)
                .deliveryStart(deliveryStart)
                .deliveryEnd(deliveryEnd)
                .build();

        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order confirm(Long orderId) {
        Order order = getById(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order cannot be confirmed");
        }
        if (order.getStatus() == OrderStatus.CONFIRMED || order.getStatus() == OrderStatus.COMPLETED) {
            return order;
        }

        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order complete(Long orderId) {
        Order order = getById(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cancelled order cannot be completed");
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return order;
        }
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED order can be completed");
        }

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = getById(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.COMPLETED) {
            return order;
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    private void validateWindow(LocalDateTime deliveryStart, LocalDateTime deliveryEnd) {
        if (deliveryStart == null || deliveryEnd == null) {
            throw new IllegalArgumentException("Delivery window is required");
        }
        if (!deliveryEnd.isAfter(deliveryStart)) {
            throw new IllegalArgumentException("deliveryEnd must be after deliveryStart");
        }
    }
}
