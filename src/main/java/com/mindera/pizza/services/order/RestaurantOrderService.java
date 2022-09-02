package com.mindera.pizza.services.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.OrderStatus;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.order.RestaurantOrderSpecifications;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.InvalidOrderStatus;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import com.mindera.pizza.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RestaurantOrderService {
    private final RestaurantOrderRepo restaurantOrderRepo;

    private final AddressRepo addressRepo;

    private final ClientRepo clientRepo;

    private final ProductRepo productRepo;

    public RestaurantOrder createOrder(CreateRestaurantOrderDTO restaurantOrderDTO) {
        Address address = addressRepo.findById(restaurantOrderDTO.addressId())
                .orElseThrow(() -> new DatabaseEntryNotFoundException("Address not found in the database"));

        Client client = clientRepo.findById(restaurantOrderDTO.clientId())
                .orElseThrow(() -> new DatabaseEntryNotFoundException("Client not found in the database"));

        RestaurantOrder restaurantOrder = RestaurantOrder.builder()
                .orderDateTime(DateTimeUtils.stringToLocalDateTime(restaurantOrderDTO.orderDateTime()))
                .address(address)
                .client(client)
                .build();

        List<Product> products = productRepo.findAllById(restaurantOrderDTO.productIds());
        if (products.isEmpty()) {
            throw new DatabaseEntryNotFoundException("Products not found in the database");
        }

        products.forEach(restaurantOrder::addProduct);

        return restaurantOrderRepo.save(restaurantOrder);
    }

    public RestaurantOrder updateOrderStatus(Long id, String newStatus) {
        OrderStatus newOrderStatus = OrderStatus.findValue(newStatus);
        if (Objects.isNull(newOrderStatus)) {
            throw new InvalidOrderStatus("The new order status is invalid. Must be one of " + Arrays.toString(OrderStatus.values()));
        }
        RestaurantOrder restaurantOrder = restaurantOrderRepo.findById(id)
                .orElseThrow(() -> new DatabaseEntryNotFoundException("Restaurant Order not found in the database"));
        switch (newOrderStatus) {
            case CANCELED -> restaurantOrder.cancelOrder();
            case ACCEPTED -> restaurantOrder.acceptOrder();
            case FINISHED -> restaurantOrder.finishOrder();
            case RECEIVED -> throw new IllegalArgumentException("Cannot change the order status to received");
        }
        return restaurantOrderRepo.save(restaurantOrder);
    }

    public List<RestaurantOrder> findOrders(Map<String, String> filters) {
        Specification<RestaurantOrder> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<RestaurantOrder> currentSpec = RestaurantOrderSpecifications.getSpecificationFromFilterName(entry.getKey(), entry.getValue());
            if (Objects.nonNull(currentSpec)) {
                spec = spec.and(currentSpec);
            }
        }

        return restaurantOrderRepo.findAll(spec);
    }

    public RestaurantOrder findOrderById(Long orderId) {
        return restaurantOrderRepo.findById(orderId).orElseThrow(() -> new DatabaseEntryNotFoundException("Order not found with the specified Id"));
    }
}
