package com.mindera.pizza.repositories.order;

import com.mindera.pizza.domain.order.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestaurantOrderRepo extends JpaRepository<RestaurantOrder, Long>, JpaSpecificationExecutor<RestaurantOrder> {
}
