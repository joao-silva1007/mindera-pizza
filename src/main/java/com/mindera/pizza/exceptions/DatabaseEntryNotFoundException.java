package com.mindera.pizza.exceptions;

import com.mindera.pizza.utils.Errors;

public class DatabaseEntryNotFoundException extends RuntimeException{
    public DatabaseEntryNotFoundException(Errors error, String className) {
        super(String.format(error.toString(), className));
    }
}
