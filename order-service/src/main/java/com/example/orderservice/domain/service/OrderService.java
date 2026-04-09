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
    public Order cancel(Long orderId) {
        Order order = getById(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED) {
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
