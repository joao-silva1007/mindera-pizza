package com.mindera.pizza.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class, DatabaseEntryNotFoundException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Object> invalidInputException(Exception ex) {
        ExceptionBody body = ExceptionBody.builder()
                .exception(ex)
                .statusCode(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(body.toMap(), HttpStatus.BAD_REQUEST);
    }
}
