package com.example.orderservice.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreateDto(
        Long userId,
        BigDecimal amount,
        LocalDateTime deliveryStart,
        LocalDateTime deliveryEnd
) {
}
