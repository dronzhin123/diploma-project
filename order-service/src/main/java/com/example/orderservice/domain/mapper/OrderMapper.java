package com.example.orderservice.domain.mapper;

import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderDto toOrderDto(Order order);
}
