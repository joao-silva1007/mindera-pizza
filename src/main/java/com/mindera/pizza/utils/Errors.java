package com.mindera.pizza.utils;

public enum Errors {
    ENTRY_NOT_FOUND("%s not found in the database"),
    ENTRY_BY_ID_NOT_FOUND("%s not found with the specified Id"),
    ILLEGAL_STATUS_CHANGE("Cannot change status from %s to %s"),
    ILLEGAL_STATUS_CHANGE_TO("Cannot change the order status to %s"),
    UNIQUE_VALUE_VIOLATION("A %s already exists with the inserted %s"),
    INVALID_ORDER_DATE_TIME("Invalid order date time"),
    INVALID_ADDRESS("Invalid address"),
    INVALID_CLIENT("Invalid client"),
    INVALID_STREET_NAME("Invalid street name"),
    INVALID_STREET_NUMBER("Invalid street number"),
    INVALID_ZIP_CODE("Invalid zip code"),
    INVALID_CITY("Invalid city"),
    INVALID_NICKNAME("Invalid nickname"),
    INVALID_APARTMENT_INFORMATION("Invalid apartment information"),
    INVALID_NAME("Invalid name"),
    INVALID_EMAIL("Invalid email"),
    INVALID_PHONE_NUMBER("Invalid phone number"),
    INVALID_VAT_NUMBER("Invalid VAT number"),
    INVALID_STOCK("Invalid stock"),
    INVALID_PRICE("Invalid price"),
    INVALID_CATEGORY("Invalid category");

    private final String description;

    Errors(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
