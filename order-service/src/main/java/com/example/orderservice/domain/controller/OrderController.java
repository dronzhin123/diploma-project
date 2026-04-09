package com.example.orderservice.domain.controller;

import com.example.orderservice.domain.dto.OrderCreateDto;
import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.mapper.OrderMapper;
import com.example.orderservice.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody OrderCreateDto dto) {
        Order order = orderService.createOrder(
                dto.userId(),
                dto.amount(),
                dto.deliveryStart(),
                dto.deliveryEnd()
        );
        return orderMapper.toOrderDto(order);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto getOrder(@PathVariable Long id) {
        return orderMapper.toOrderDto(orderService.getOrder(id));
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto cancelOrder(@PathVariable Long id) {
        return orderMapper.toOrderDto(orderService.cancelOrder(id));
    }
}
