package com.mindera.pizza.repositories.order;

import com.mindera.pizza.domain.order.OrderStatus;
import com.mindera.pizza.domain.order.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<RestaurantOrder, Long> {
    List<RestaurantOrder> getRestaurantOrdersByClient_VatNumber(String vatNumber);

    List<RestaurantOrder> getRestaurantOrdersByClient_Name(String name);

    List<RestaurantOrder> getRestaurantOrdersByClient_PhoneNumber(String phoneNumber);

    List<RestaurantOrder> getRestaurantOrdersByCurrentStatusEquals(OrderStatus orderStatus);
}
