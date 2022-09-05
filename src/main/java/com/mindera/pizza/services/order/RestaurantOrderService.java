package com.mindera.pizza.services.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.order.RestaurantOrderSpecifications;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.dto.order.UpdateRestaurantOrderStatusDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import com.mindera.pizza.utils.DateTimeUtils;
import com.mindera.pizza.utils.Errors;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new DatabaseEntryNotFoundException(String.format(Errors.ENTRY_NOT_FOUND.toString(), Address.class.getSimpleName())));

        Client client = clientRepo.findById(restaurantOrderDTO.clientId())
                .orElseThrow(() -> new DatabaseEntryNotFoundException(String.format(Errors.ENTRY_NOT_FOUND.toString(), Client.class.getSimpleName())));

        RestaurantOrder restaurantOrder = RestaurantOrder.builder()
                .orderDateTime(DateTimeUtils.stringToLocalDateTime(restaurantOrderDTO.orderDateTime()))
                .address(address)
                .client(client)
                .build();

        List<Product> products = productRepo.findAllById(restaurantOrderDTO.productIds());
        if (products.isEmpty()) {
            throw new DatabaseEntryNotFoundException(String.format(Errors.ENTRY_NOT_FOUND.toString(), Product.class.getSimpleName()));
        }

        products.forEach(restaurantOrder::addProduct);

        return restaurantOrderRepo.save(restaurantOrder);
    }

    public RestaurantOrder updateOrderStatus(Long id, UpdateRestaurantOrderStatusDTO updateRestaurantOrderStatusDTO) {
        RestaurantOrder restaurantOrder = restaurantOrderRepo.findById(id)
                .orElseThrow(() -> new DatabaseEntryNotFoundException(String.format(Errors.ENTRY_NOT_FOUND.toString(), RestaurantOrder.class.getSimpleName())));

        switch (updateRestaurantOrderStatusDTO.newStatus()) {
            case CANCELED -> restaurantOrder.cancelOrder();
            case ACCEPTED -> restaurantOrder.acceptOrder();
            case FINISHED -> restaurantOrder.finishOrder();
            case RECEIVED -> throw new IllegalArgumentException(Errors.ILLEGAL_STATUS_CHANGE_TO_RECEIVED.toString());
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
        return restaurantOrderRepo.findById(orderId).orElseThrow(() -> new DatabaseEntryNotFoundException(String.format(Errors.ENTRY_BY_ID_NOT_FOUND.toString(), RestaurantOrder.class.getSimpleName())));
    }
}
