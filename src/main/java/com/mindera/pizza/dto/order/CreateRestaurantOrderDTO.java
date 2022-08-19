package com.mindera.pizza.dto.order;

import lombok.Getter;

import java.util.List;

public record CreateRestaurantOrderDTO(String orderDateTime, List<Long> productIds,
                                       Long addressId, Long clientId) {
}