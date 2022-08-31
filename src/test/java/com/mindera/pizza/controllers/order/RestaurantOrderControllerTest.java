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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantOrderControllerTest {

    RestaurantOrderService restaurantOrderServiceMock;
    RestaurantOrderController restaurantOrderController;

    @BeforeEach
    void beforeEach() {
        restaurantOrderServiceMock = Mockito.mock(RestaurantOrderService.class);
        restaurantOrderController = new RestaurantOrderController(restaurantOrderServiceMock);
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

    @Test
    void getOrdersWithNoFilter() {
        Client c = new Client("name", "email@gmail.com");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);

        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),a, c);
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,4,15,10,10,10),a, c);
        ro.addProduct(p);
        ro2.addProduct(p);
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro, ro2), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro, ro2));
        Map<String, String> params = new HashMap<>();
        assertEquals(expected, restaurantOrderController.getRestaurantOrders(params));
    }

    @Test
    void getOrdersWithClientName() {
        Client c = new Client("name", "email@gmail.com");
        c.setPhoneNumber("915487569");
        Client c2 = new Client("name2", "email@gmail.com");
        c2.setPhoneNumber("915354569");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);

        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),a, c);
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,4,15,10,10,10),a, c2);
        ro.addProduct(p);
        ro2.addProduct(p);
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro2), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("clientName", "name2");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro2));

        Map<String, String> params = new HashMap<>();
        params.put("clientName", "name2");
        assertEquals(expected, restaurantOrderController.getRestaurantOrders(params));
    }

    @Test
    void getOrdersWithPhoneNumber() {
        Client c = new Client("name", "email@gmail.com");
        c.setPhoneNumber("915487569");
        Client c2 = new Client("name2", "email@gmail.com");
        c2.setPhoneNumber("915354569");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);

        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),a, c);
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,4,15,10,10,10),a, c2);
        ro.addProduct(p);
        ro2.addProduct(p);
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("phoneNumber", "915487569");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro));

        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", "915487569");
        assertEquals(expected, restaurantOrderController.getRestaurantOrders(params));
    }

    @Test
    void getOrdersWithVatNumber() {
        Client c = new Client("name", "email@gmail.com");
        c.setPhoneNumber("915487569");
        c.setVatNumber("245784569");
        Client c2 = new Client("name2", "email@gmail.com");
        c2.setPhoneNumber("915354569");
        c2.setVatNumber("325478569");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);

        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),a, c);
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,4,15,10,10,10),a, c2);
        ro.addProduct(p);
        ro2.addProduct(p);
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("vatNumber", "325478569");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro));

        Map<String, String> params = new HashMap<>();
        params.put("vatNumber", "325478569");
        assertEquals(expected, restaurantOrderController.getRestaurantOrders(params));
    }

    @Test
    void getOrdersWithOrderStatus() {
        Client c = new Client("name", "email@gmail.com");
        c.setPhoneNumber("915487569");
        c.setVatNumber("245784569");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);

        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),a, c);
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,4,15,10,10,10),a, c);
        ro2.cancelOrder();
        ro.addProduct(p);
        ro2.addProduct(p);
        ResponseEntity<List<RestaurantOrder>> expected = new ResponseEntity<>(List.of(ro), HttpStatus.OK);
        Map<String, String> filters = new HashMap<>();
        filters.put("currentStatus", "RECEIVED");
        Mockito.when(restaurantOrderServiceMock.findOrders(filters)).thenReturn(List.of(ro));

        Map<String, String> params = new HashMap<>();
        params.put("currentStatus", "RECEIVED");
        assertEquals(expected, restaurantOrderController.getRestaurantOrders(params));
    }
}