package com.mindera.pizza.services.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantOrderServiceTest {

    ClientRepo clientRepoMock;
    AddressRepo addressRepoMock;
    ProductRepo productRepoMock;
    RestaurantOrderRepo restaurantOrderRepoMock;

    @BeforeEach
    void beforeAll() {
        clientRepoMock = Mockito.mock(ClientRepo.class);
        addressRepoMock = Mockito.mock(AddressRepo.class);
        productRepoMock = Mockito.mock(ProductRepo.class);
        restaurantOrderRepoMock = Mockito.mock(RestaurantOrderRepo.class);

        Client c = new Client("name", "email@gmail.com");
        Mockito.when(clientRepoMock.findById(1L)).thenReturn(Optional.of(c));
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        Mockito.when(addressRepoMock.findById(1L)).thenReturn(Optional.of(a));
        Category cat = new Category("cat1");
        Product p = new Product("prod", 10, 10, cat);
        Mockito.when(productRepoMock.findAllById(List.of(1L))).thenReturn(List.of(p));
    }

    @Test
    void createValidOrder() {
        Client c = new Client("name", "email@gmail.com");
        Address a = new Address("street", 10, "1234-123", "city", "house", c);
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);
        RestaurantOrder expectedRo = new RestaurantOrder(LocalDateTime.of(2021,10,10,10,10,10),a, c);
        Mockito.when(restaurantOrderRepoMock.save(Mockito.any(RestaurantOrder.class))).thenReturn(expectedRo);
        RestaurantOrder ro = service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", List.of(1L), 1L, 1L));
        Mockito.verify(restaurantOrderRepoMock, Mockito.times(1)).save(Mockito.any());
        assertEquals(expectedRo, ro);
    }

    @Test
    void nonExistentClient() {
        ClientRepo newClientRepoMock = Mockito.mock(ClientRepo.class);
        RestaurantOrderRepo restaurantOrderRepoMock = Mockito.mock(RestaurantOrderRepo.class);
        Mockito.when(newClientRepoMock.findById(1L)).thenReturn(Optional.empty());
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, newClientRepoMock, productRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> {
            service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", List.of(1L), 1L, 1L));
        });
    }

    @Test
    void nonExistentAddress() {
        AddressRepo newAddressRepoMock = Mockito.mock(AddressRepo.class);

        Mockito.when(newAddressRepoMock.findById(1L)).thenReturn(Optional.empty());
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, newAddressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> {
            service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", List.of(1L), 1L, 1L));
        });
    }

    @Test
    void nonExistentProducts() {
        ProductRepo newProductRepoMock = Mockito.mock(ProductRepo.class);

        Mockito.when(newProductRepoMock.findAllById(Arrays.asList(1L,2L))).thenReturn(new LinkedList<>());
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, newProductRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> {
            service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", Arrays.asList(1L,2L), 1L, 1L));
        });
    }
}