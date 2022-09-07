package com.mindera.pizza.utils;

public enum Errors {
    ENTRY_NOT_FOUND("%s not found in the database"),
    ENTRY_BY_ID_NOT_FOUND("%s not found with the specified Id"),
    ILLEGAL_STATUS_CHANGE("Cannot change status from %s to %s"),
    ILLEGAL_STATUS_CHANGE_TO("Cannot change the order status to %s"),
    UNIQUE_VALUE_VIOLATION("A %s already exists with the inserted %s"),
    INVALID_ORDER_DATE_TIME("Invalid order date time");

    private final String description;

    Errors(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
