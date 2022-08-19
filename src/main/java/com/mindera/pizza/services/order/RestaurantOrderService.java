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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantOrderService {
    private final RestaurantOrderRepo restaurantOrderRepo;

    private final AddressRepo addressRepo;

    private final ClientRepo clientRepo;

    private final ProductRepo productRepo;

    public RestaurantOrderService(RestaurantOrderRepo restaurantOrderRepo, AddressRepo addressRepo, ClientRepo clientRepo, ProductRepo productRepo) {
        this.restaurantOrderRepo = restaurantOrderRepo;
        this.addressRepo = addressRepo;
        this.clientRepo = clientRepo;
        this.productRepo = productRepo;
    }

    public RestaurantOrder createOrder(CreateRestaurantOrderDTO restaurantOrderDTO) {
        Optional<Address> address = addressRepo.findById(restaurantOrderDTO.addressId());
        if (address.isEmpty()) {
            throw new DatabaseEntryNotFoundException("Address not found in the database");
        }

        Optional<Client> client = clientRepo.findById(restaurantOrderDTO.clientId());
        if (client.isEmpty()) {
            throw new DatabaseEntryNotFoundException("Client not found in the database");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime orderDateTime = LocalDateTime.parse(restaurantOrderDTO.orderDateTime(), formatter);
        RestaurantOrder restaurantOrder = new RestaurantOrder(orderDateTime, address.get(), client.get());

        List<Product> products = productRepo.findAllById(restaurantOrderDTO.productIds());
        if (products.size() == 0) {
            throw new DatabaseEntryNotFoundException("Products not found in the database");
        }

        for (Product product : products) {
            restaurantOrder.addProduct(product);
        }
        return restaurantOrderRepo.save(restaurantOrder);
    }
}
