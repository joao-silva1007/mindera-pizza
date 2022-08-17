package com.mindera.pizza.domain.order;

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
}
