package com.mindera.pizza.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class, DatabaseEntryNotFoundException.class})
    public ResponseEntity<Object> invalidInputException(Exception ex) {
        return new ResponseEntity<>(getExceptionBody(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> getExceptionBody(Exception e, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("timestamp",LocalDateTime.now().toString());
        map.put("status", status.toString());
        map.put("errorMessage", e.getMessage());
        return map;
    }
}
