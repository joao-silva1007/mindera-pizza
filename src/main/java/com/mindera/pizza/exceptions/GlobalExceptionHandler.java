package com.mindera.pizza.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class, DatabaseEntryNotFoundException.class, InvalidStatusChangeException.class, UniqueValueViolationException.class})
    public ResponseEntity<Object> invalidInputException(Exception ex) {
        ExceptionBody body = ExceptionBody.builder()
                .exception(ex)
                .statusCode(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        logger.error(ex.getStackTrace()[0]);
        logger.error(ex.getMessage());
        return new ResponseEntity<>(body.toMap(), HttpStatus.BAD_REQUEST);
    }
}
