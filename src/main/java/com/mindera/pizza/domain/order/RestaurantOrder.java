package com.mindera.pizza.domain.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode
public class RestaurantOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private LocalDateTime orderDateTime;

    @Getter @Setter
    private float totalPrice;

    @ManyToOne
    @Getter @Setter
    private Address address;

    @ManyToOne
    @Getter @Setter
    private Client client;

    @Getter
    private LocalDateTime createdAt;

    @Getter @Setter
    private LocalDateTime updatedAt;

    protected RestaurantOrder() {}
}
