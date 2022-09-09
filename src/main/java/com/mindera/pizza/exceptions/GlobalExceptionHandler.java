package com.mindera.pizza.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {IllegalArgumentException.class, DatabaseEntryNotFoundException.class, InvalidStatusChangeException.class, UniqueValueViolationException.class})
    public ResponseEntity<Object> invalidInputException(Exception ex) {
        ExceptionBody body = ExceptionBody.builder()
                .exception(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        logger.error(ex.getStackTrace()[0]);
        logger.error(ex.getMessage());
        return new ResponseEntity<>(body.toMap(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> dataValidationError(ConstraintViolationException exception) {
        List<String> validationErrors = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        ExceptionBody body = ExceptionBody.builder()
                .exception(validationErrors)
                .statusCode(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(body.toMap(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> dtoValidationError(MethodArgumentNotValidException exception) {
        ExceptionBody body = ExceptionBody.builder()
                .exception(exception.getParameter())
                .statusCode(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(body.toMap(), HttpStatus.BAD_REQUEST);
    }
}
