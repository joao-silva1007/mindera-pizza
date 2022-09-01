package com.mindera.pizza.exceptions;

public class InvalidOrderStatus extends IllegalArgumentException {
    public InvalidOrderStatus(String message) {
        super(message);
    }
}
