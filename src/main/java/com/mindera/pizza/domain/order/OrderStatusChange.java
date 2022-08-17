package com.mindera.pizza.domain.order;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
@EqualsAndHashCode
public class OrderStatusChange {
    @Enumerated(EnumType.STRING)
    @Getter
    private OrderStatus orderStatus;

    @Getter
    private LocalDateTime changeDateTime;

    protected OrderStatusChange() {}

    public OrderStatusChange(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.changeDateTime = LocalDateTime.now();
    }
}
