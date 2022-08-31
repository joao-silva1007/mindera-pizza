package com.mindera.pizza.controllers.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.services.order.RestaurantOrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantOrderControllerTest {

    RestaurantOrderService restaurantOrderServiceMock;
    RestaurantOrderController restaurantOrderController;

    static RestaurantOrder ro1;
    static RestaurantOrder ro2;
    static Client client1;
    static Client client2;
    static Address address;
    static Address address2;
    static Product product;
    static Category cat;

    @BeforeAll
    static void beforeAll() {
        client1 = new Client("name", "email@gmail.com");
        client1.setPhoneNumber("915487569");
        client1.setVatNumber("245784569");
        client2 = new Client("name2", "email@gmail.com");
        client2.setPhoneNumber("915354569");
        client2.setVatNumber("325478569");
        address = new Address("street", 10, "1234-123", "city", "house", client1);
        address2 = new Address("streetABC", 10, "1234-465", "city", "house", client2);
        cat = new Category("cat1");
        product = new Product("prod", 10, 10, cat);

        ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),address, client1);
        ro2 = new RestaurantOrder(LocalDateTime.of(2022,4,15,10,10,10),address2, client2);
        ro1.addProduct(product);
        ro2.addProduct(product);
    }

    @BeforeEach
    void beforeEach() {
        restaurantOrderServiceMock = Mockito.mock(RestaurantOrderService.class);
        restaurantOrderController = new RestaurantOrderController(restaurantOrderServiceMock);
    }

    @Test
    void createRestaurantOrder() {
        CreateRestaurantOrderDTO dto = new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 1L);

        Mockito.when(restaurantOrderServiceMock.createOrder(dto)).thenReturn(ro1);
        ResponseEntity<RestaurantOrder> expected = new ResponseEntity<>(ro1, HttpStatus.CREATED);
    }

    @Test
    void getOrdersWithNoFilter() {
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro1, ro2), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro1, ro2));
        assertEquals(expected, restaurantOrderController.getRestaurantOrders(filters));
    }

    @Test
    void getOrdersWithClientName() {
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro2), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro2));

        assertEquals(expected, restaurantOrderController.getRestaurantOrders(filters));
    }

    @Test
    void getOrdersWithPhoneNumber() {
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro1), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("phoneNumber", "915487569");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro1));

        assertEquals(expected, restaurantOrderController.getRestaurantOrders(filters));
    }

    @Test
    void getOrdersWithVatNumber() {
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro1), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("vatNumber", "325478569");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro1));

        assertEquals(expected, restaurantOrderController.getRestaurantOrders(filters));
    }

    @Test
    void getOrdersWithOrderStatus() {
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro1), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("currentStatus", "RECEIVED");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro1));

        assertEquals(expected, restaurantOrderController.getRestaurantOrders(filters));
    }
}