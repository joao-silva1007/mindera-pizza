package com.mindera.pizza.controllers.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.services.order.RestaurantOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

class RestaurantOrderControllerTest {

    RestaurantOrderService restaurantOrderServiceMock;

    @BeforeEach
    void beforeEach() {
        restaurantOrderServiceMock = Mockito.mock(RestaurantOrderService.class);
    }

    @Test
    void createRestaurantOrder() {
        CreateRestaurantOrderDTO dto = new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 1L);

        Client c = new Client("name", "email@gmail.com");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);

        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),a, c);
        ro.addProduct(p);
        Mockito.when(restaurantOrderServiceMock.createOrder(dto)).thenReturn(ro);
        ResponseEntity<RestaurantOrder> expected = new ResponseEntity<>(ro, HttpStatus.CREATED);
    }
}