package com.mindera.pizza.utils;

public enum LoggingMessages {
    ENTRY_ADDED_TO_DB("Added a new {} with id {} to the DB"),
    UNIQUE_ENTRY_VIOLATION("Attempted to create a {} with an existing {} ({})"),
    SINGLE_ENTRY_FETCHED_FROM_DB("Fetched {} with id {}"),
    ENTRIES_FETCHED_FROM_DB("Fetched {} {} from the DB");

    private final String description;

    LoggingMessages(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}