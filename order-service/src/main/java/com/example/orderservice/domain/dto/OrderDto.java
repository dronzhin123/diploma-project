package com.example.orderservice.domain.dto;

import com.example.orderservice.domain.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
        Long id,
        Long userId,
        BigDecimal amount,
        OrderStatus status,
        LocalDateTime deliveryStart,
        LocalDateTime deliveryEnd
) {
}
