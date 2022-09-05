package com.mindera.pizza.exceptions;

public class InvalidStatusChangeException extends IllegalArgumentException{
    public InvalidStatusChangeException(String message) {
        super(message);
    }
}
