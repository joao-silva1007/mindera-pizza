package com.mindera.pizza.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
public class ExceptionBody {
    private LocalDateTime timestamp;
    private Object exception;
    private HttpStatus statusCode;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp",this.timestamp.toString());
        map.put("status", statusCode.toString());
        map.put("errorMessage", exception);
        return map;
    }
}
