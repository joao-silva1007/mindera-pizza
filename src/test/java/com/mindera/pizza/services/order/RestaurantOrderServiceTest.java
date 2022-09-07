package com.mindera.pizza.services.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.OrderStatus;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.dto.order.UpdateRestaurantOrderStatusDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.InvalidStatusChangeException;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantOrderServiceTest {

    static ClientRepo clientRepoMock;
    static AddressRepo addressRepoMock;
    static ProductRepo productRepoMock;
    static RestaurantOrderRepo restaurantOrderRepoMock;

    static Client c;
    static Address a;
    static Category cat;
    static Product p;

    static RestaurantOrder sampleRestaurantOrder;

    @BeforeEach
    void beforeEach() {
        clientRepoMock = Mockito.mock(ClientRepo.class);
        addressRepoMock = Mockito.mock(AddressRepo.class);
        productRepoMock = Mockito.mock(ProductRepo.class);
        restaurantOrderRepoMock = Mockito.mock(RestaurantOrderRepo.class);

        c = new Client("name", "email@gmail.com");
        Mockito.when(clientRepoMock.findById(1L)).thenReturn(Optional.of(c));
        a = new Address("street", 10, "1234-123", "city", "house", c);
        Mockito.when(addressRepoMock.findById(1L)).thenReturn(Optional.of(a));
        cat = new Category("cat1");
        p = new Product("prod", 10, 10, cat);
        Mockito.when(productRepoMock.findAllById(List.of(1L))).thenReturn(List.of(p));

        sampleRestaurantOrder = new RestaurantOrder(LocalDateTime.of(2020,10,10,10,10,10),a, c);
        Mockito.when(restaurantOrderRepoMock.findById(1L)).thenReturn(Optional.of(sampleRestaurantOrder));
    }

    @Test
    void createValidOrder() {
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);
        RestaurantOrder expectedRo = new RestaurantOrder(LocalDateTime.of(2021,10,10,10,10,10),a, c);
        Mockito.when(restaurantOrderRepoMock.save(Mockito.any(RestaurantOrder.class))).thenReturn(expectedRo);
        RestaurantOrder ro = service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", List.of(1L), 1L, 1L));
        Mockito.verify(restaurantOrderRepoMock, Mockito.times(1)).save(Mockito.any());
        assertEquals(expectedRo, ro);
    }

    @Test
    void createOrderWithNonExistentClient() {
        ClientRepo newClientRepoMock = Mockito.mock(ClientRepo.class);
        RestaurantOrderRepo restaurantOrderRepoMock = Mockito.mock(RestaurantOrderRepo.class);
        Mockito.when(newClientRepoMock.findById(1L)).thenReturn(Optional.empty());
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, newClientRepoMock, productRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", List.of(1L), 1L, 1L)));
    }

    @Test
    void createOrderWithNonExistentAddress() {
        AddressRepo newAddressRepoMock = Mockito.mock(AddressRepo.class);

        Mockito.when(newAddressRepoMock.findById(1L)).thenReturn(Optional.empty());
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, newAddressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", List.of(1L), 1L, 1L)));
    }

    @Test
    void createOrderWithNonExistentProducts() {
        ProductRepo newProductRepoMock = Mockito.mock(ProductRepo.class);

        Mockito.when(newProductRepoMock.findAllById(Arrays.asList(1L,2L))).thenReturn(new LinkedList<>());
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, newProductRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> service.createOrder(new CreateRestaurantOrderDTO("2021-10-10 10:10:10", Arrays.asList(1L,2L), 1L, 1L)));
    }

    @Test
    void getOrdersNoFilters() {
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2021,10,10,10,10,10),a, c);
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2021,10,15,10,10,10),a, c);
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2021,10,20,10,10,10),a, c);
        List<RestaurantOrder> expectedResult = List.of(ro1, ro2, ro3);

        RestaurantOrderRepo newRestaurantOrderRepoMock = Mockito.mock(RestaurantOrderRepo.class);

        RestaurantOrderService service = new RestaurantOrderService(newRestaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        Mockito.when(newRestaurantOrderRepoMock.findAll((Specification<RestaurantOrder>) Mockito.any())).thenReturn(expectedResult);
        Map<String, String> filters = new HashMap<>();
        List<RestaurantOrder> result = service.findOrders(filters);

        assertEquals(expectedResult, result);
    }

    @Test
    void findExistingOrderById() {
        RestaurantOrder expectedRo = new RestaurantOrder(LocalDateTime.of(2020,10,10,10,10,10),a, c);

        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        RestaurantOrder ro = service.findOrderById(1L);
        Mockito.verify(restaurantOrderRepoMock, Mockito.times(1)).findById(1L);
        assertEquals(expectedRo, ro);
    }

    @Test
    void findNonExistingOrderById() {
        RestaurantOrderRepo newRestaurantOrderRepoMock = Mockito.mock(RestaurantOrderRepo.class);

        Mockito.when(newRestaurantOrderRepoMock.findById(Mockito.any())).thenReturn(Optional.empty());
        RestaurantOrderService service = new RestaurantOrderService(newRestaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(DatabaseEntryNotFoundException.class, () -> service.findOrderById(1L));
    }

    @Test
    void updateOrderStatusToCanceled() {
        RestaurantOrder actual = new RestaurantOrder(LocalDateTime.of(2020,10,10,10,10,10),a, c);
        actual.cancelOrder();

        Mockito.when(restaurantOrderRepoMock.save(Mockito.any(RestaurantOrder.class))).thenReturn(actual);
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);
        assertEquals(sampleRestaurantOrder, service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.CANCELED)));
    }

    @Test
    void updateOrderStatusToFinished() {
        RestaurantOrder actualRo = new RestaurantOrder(LocalDateTime.of(2020,10,10,10,10,10),a, c);
        actualRo.acceptOrder();
        actualRo.finishOrder();

        sampleRestaurantOrder.acceptOrder();

        Mockito.when(restaurantOrderRepoMock.save(Mockito.any(RestaurantOrder.class))).thenReturn(actualRo);
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertEquals(sampleRestaurantOrder, service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.FINISHED)));
    }

    @Test
    void updateOrderStatusToAccepted() {
        RestaurantOrder actual = new RestaurantOrder(LocalDateTime.of(2020,10,10,10,10,10),a, c);
        actual.acceptOrder();

        Mockito.when(restaurantOrderRepoMock.save(Mockito.any(RestaurantOrder.class))).thenReturn(actual);
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertEquals(sampleRestaurantOrder, service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.ACCEPTED)));
    }

    @Test
    void updateOrderStatusToReceived() {
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(IllegalArgumentException.class, () -> service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.RECEIVED)));
    }

    @Test
    void updateOrderStatusFromReceivedToFinished() {
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(InvalidStatusChangeException.class, () -> service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.FINISHED)));
    }

    @Test
    void updateOrderStatusFromFinishedToCanceled() {
        sampleRestaurantOrder.acceptOrder();
        sampleRestaurantOrder.finishOrder();
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(InvalidStatusChangeException.class, () -> service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.CANCELED)));
    }

    @Test
    void updateOrderStatusFromFinishedToAccepted() {
        sampleRestaurantOrder.acceptOrder();
        sampleRestaurantOrder.finishOrder();
        RestaurantOrderService service = new RestaurantOrderService(restaurantOrderRepoMock, addressRepoMock, clientRepoMock, productRepoMock);

        assertThrows(InvalidStatusChangeException.class, () -> service.updateOrderStatus(1L, new UpdateRestaurantOrderStatusDTO(OrderStatus.ACCEPTED)));
    }
}