package com.mindera.pizza.domain.order;

import java.util.Objects;

public enum OrderStatus {
    RECEIVED("Received"),
    ACCEPTED("Accepted"),
    CANCELED("Canceled"),
    FINISHED("Finished");

    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return this.orderStatus;
    }

    public static OrderStatus findValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }

        try {
            return OrderStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}