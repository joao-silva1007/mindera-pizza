package com.mindera.pizza.controllers.order;

import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.services.order.RestaurantOrderService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class RestaurantOrderController {
    private final RestaurantOrderService restaurantOrderService;

    @PostMapping
    public ResponseEntity<RestaurantOrder> createRestaurantOrder(@RequestBody CreateRestaurantOrderDTO restaurantOrderDTO) {
        return new ResponseEntity<>(restaurantOrderService.createOrder(restaurantOrderDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantOrder>> getRestaurantOrders(@RequestParam(required = false) Map<String, String> params) {
        return new ResponseEntity<>(restaurantOrderService.findOrders(params), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RestaurantOrder> findOrderById(@PathVariable Long id) {
        return new ResponseEntity<>(restaurantOrderService.findOrderById(id), HttpStatus.OK);
    }
}
