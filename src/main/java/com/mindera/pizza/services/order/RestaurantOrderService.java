package com.mindera.pizza.services.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import com.mindera.pizza.utils.DateTimeUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
