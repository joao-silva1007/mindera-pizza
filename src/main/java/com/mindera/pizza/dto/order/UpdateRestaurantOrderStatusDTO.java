package com.mindera.pizza.dto.order;

import com.mindera.pizza.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRestaurantOrderStatusDTO {
    OrderStatus newStatus;
}
