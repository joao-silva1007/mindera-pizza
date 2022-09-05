package com.mindera.pizza.domain.order;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.exceptions.InvalidStatusChangeException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = {"products", "timestamps"})
public class RestaurantOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "restaurantorder_product",
            joinColumns = @JoinColumn(name = "restaurantorder_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    @Getter
    private Set<Product> products;

    @Getter
    private OrderStatus currentStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    @Getter
    private List<OrderStatusChange> orderStatusChanges;

    @Getter
    @Embedded
    private final DatabaseTimestamps timestamps = new DatabaseTimestamps();

    protected RestaurantOrder() {}

    @Builder
    public RestaurantOrder(LocalDateTime orderDateTime, Address address, Client client) {
        if (orderDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid order date time");
        }

        if (address == null) {
            throw new IllegalArgumentException("Invalid address");
        }

        if (client == null) {
            throw new IllegalArgumentException("Invalid client");
        }
        this.orderDateTime = orderDateTime;
        this.totalPrice = 0;
        this.address = address;
        this.client = client;
        this.products = new HashSet<>();
        this.orderStatusChanges = new LinkedList<>();
        this.orderStatusChanges.add(new OrderStatusChange(OrderStatus.RECEIVED));
        this.currentStatus = OrderStatus.RECEIVED;
    }

    public boolean addProduct(Product product) {
        if (product == null) return false;
        this.totalPrice += product.getPrice();
        return products.add(product);
    }

    public void finishOrder() {
        if (this.currentStatus != OrderStatus.ACCEPTED) {
            throw new InvalidStatusChangeException(String.format("Cannot change status from %s to %s", this.currentStatus, OrderStatus.FINISHED));
        }
        changeStatus(OrderStatus.FINISHED);
    }

    public void cancelOrder() {
        if (!List.of(OrderStatus.RECEIVED, OrderStatus.ACCEPTED).contains(this.currentStatus)) {
            throw new InvalidStatusChangeException(String.format("Cannot change status from %s to %s", this.currentStatus, OrderStatus.CANCELED));
        }
        changeStatus(OrderStatus.CANCELED);
    }

    public void acceptOrder() {
        if (this.currentStatus != OrderStatus.RECEIVED) {
            throw new InvalidStatusChangeException(String.format("Cannot change status from %s to %s", this.currentStatus, OrderStatus.ACCEPTED));
        }
        changeStatus(OrderStatus.ACCEPTED);
    }

    private void changeStatus(OrderStatus newStatus) {
        this.currentStatus = newStatus;
        this.orderStatusChanges.add(new OrderStatusChange(newStatus));
    }
}
