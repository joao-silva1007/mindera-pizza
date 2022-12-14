package com.mindera.pizza.services.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.OrderStatus;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.order.RestaurantOrderSpecifications;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.dto.order.UpdateRestaurantOrderStatusDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.GlobalExceptionHandler;
import com.mindera.pizza.exceptions.InvalidStatusChangeException;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import com.mindera.pizza.utils.DateTimeUtils;
import com.mindera.pizza.utils.Errors;
import com.mindera.pizza.utils.LoggingMessages;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
@Validated
public class RestaurantOrderService {
    private static final Logger logger = LogManager.getLogger(RestaurantOrderService.class);

    private final RestaurantOrderRepo restaurantOrderRepo;

    private final AddressRepo addressRepo;

    private final ClientRepo clientRepo;

    private final ProductRepo productRepo;

    public RestaurantOrder createOrder(@Valid CreateRestaurantOrderDTO restaurantOrderDTO) {
        Address address = addressRepo.findById(restaurantOrderDTO.getAddressId())
                .orElseThrow(() -> new DatabaseEntryNotFoundException(Errors.ENTRY_NOT_FOUND, Address.class.getSimpleName()));

        Client client = clientRepo.findById(restaurantOrderDTO.getClientId())
                .orElseThrow(() -> new DatabaseEntryNotFoundException(Errors.ENTRY_NOT_FOUND, Client.class.getSimpleName()));

        RestaurantOrder restaurantOrder = RestaurantOrder.builder()
                .orderDateTime(DateTimeUtils.stringToLocalDateTime(restaurantOrderDTO.getOrderDateTime()))
                .address(address)
                .client(client)
                .build();

        List<Product> products = productRepo.findAllById(restaurantOrderDTO.getProductIds());
        if (products.isEmpty()) {
            throw new DatabaseEntryNotFoundException(Errors.ENTRY_NOT_FOUND, Product.class.getSimpleName());
        }

        products.forEach(restaurantOrder::addProduct);

        RestaurantOrder savedRO = restaurantOrderRepo.save(restaurantOrder);

        logger.info(LoggingMessages.ENTRY_ADDED_TO_DB.toString(), RestaurantOrder.class.getSimpleName(), savedRO.getId());
        return savedRO;
    }

    public RestaurantOrder updateOrderStatus(Long id, @Valid UpdateRestaurantOrderStatusDTO updateRestaurantOrderStatusDTO) {
        RestaurantOrder restaurantOrder = restaurantOrderRepo.findById(id)
                .orElseThrow(() -> new DatabaseEntryNotFoundException(Errors.ENTRY_NOT_FOUND, RestaurantOrder.class.getSimpleName()));

        switch (updateRestaurantOrderStatusDTO.getNewStatus()) {
            case CANCELED -> restaurantOrder.cancelOrder();
            case ACCEPTED -> restaurantOrder.acceptOrder();
            case FINISHED -> restaurantOrder.finishOrder();
            case RECEIVED -> throw new InvalidStatusChangeException(Errors.ILLEGAL_STATUS_CHANGE_TO, OrderStatus.RECEIVED);
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

        List<RestaurantOrder> restaurantOrders = restaurantOrderRepo.findAll(spec);
        logger.info(LoggingMessages.ENTRIES_FETCHED_FROM_DB.toString(), restaurantOrders.size(), RestaurantOrder.class.getSimpleName());
        return restaurantOrders;
    }

    public RestaurantOrder findOrderById(Long orderId) {
        RestaurantOrder restaurantOrder = restaurantOrderRepo.findById(orderId).orElseThrow(() -> new DatabaseEntryNotFoundException(Errors.ENTRY_BY_ID_NOT_FOUND, RestaurantOrder.class.getSimpleName()));
        logger.info(LoggingMessages.SINGLE_ENTRY_FETCHED_FROM_DB.toString(), RestaurantOrder.class.getSimpleName(), orderId);
        return restaurantOrder;
    }
}
