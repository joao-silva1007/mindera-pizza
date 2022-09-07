package com.mindera.pizza.dto.order;

import com.mindera.pizza.domain.order.OrderStatus;
import lombok.Builder;

public record UpdateRestaurantOrderStatusDTO(OrderStatus newStatus) {
    @Builder public UpdateRestaurantOrderStatusDTO {}
}
