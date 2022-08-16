package com.mindera.pizza.repositories.order;

import com.mindera.pizza.domain.order.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<RestaurantOrder, Long> {
    @Query("select ro from RestaurantOrder ro where ro.client.vatNumber = ?1")
    List<RestaurantOrder> getOrdersByClientVatNumber(String vatNumber);

    @Query("select ro from RestaurantOrder ro where ro.client.name = ?1")
    List<RestaurantOrder> getOrdersByClientName(String name);

    @Query("select ro from RestaurantOrder ro where ro.client.phoneNumber = ?1")
    List<RestaurantOrder> getOrdersByClientPhoneNumber(String phoneNumber);

}
