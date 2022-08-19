package com.mindera.pizza.exceptions;

public class DatabaseEntryNotFoundException extends RuntimeException{
    public DatabaseEntryNotFoundException(String message) {
        super(message);
    }
}
