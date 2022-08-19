package com.mindera.pizza.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
public class ExceptionBody {
    private LocalDateTime timestamp;
    private Exception exception;
    private HttpStatus statusCode;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("timestamp",this.timestamp.toString());
        map.put("status", statusCode.toString());
        map.put("errorMessage", exception.getMessage());
        return map;
    }
}
