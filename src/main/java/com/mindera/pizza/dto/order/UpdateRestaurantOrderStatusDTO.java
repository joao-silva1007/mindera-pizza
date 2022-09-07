package com.mindera.pizza.dto.order;

import com.mindera.pizza.domain.order.OrderStatus;

public record UpdateRestaurantOrderStatusDTO(OrderStatus newStatus) {
}
