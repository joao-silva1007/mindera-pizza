package com.mindera.pizza.exceptions;

import com.mindera.pizza.utils.Errors;
import org.springframework.dao.DataIntegrityViolationException;

public class UniqueValueViolationException extends DataIntegrityViolationException {
    public UniqueValueViolationException(String className, String valueName) {
        super(String.format(Errors.UNIQUE_VALUE_VIOLATION.toString(), className, valueName));
    }
}
