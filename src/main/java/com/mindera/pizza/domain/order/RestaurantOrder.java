package com.mindera.pizza.domain.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.product.Product;
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
@EqualsAndHashCode(exclude = "products")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "restaurantorder_product",
            joinColumns = @JoinColumn(name = "restaurantorder_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    @Getter
    private Set<Product> products;

    @ElementCollection
    @Getter
    private List<OrderStatusChange> orderStatusChanges;

    @Getter
    private LocalDateTime createdAt;

    @Getter @Setter
    private LocalDateTime updatedAt;

    protected RestaurantOrder() {}

    public RestaurantOrder(LocalDateTime orderDateTime, float totalPrice, Address address, Client client) {
        if (orderDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid order date time");
        }

        if (totalPrice < 0) {
            throw new IllegalArgumentException("Invalid total price");
        }

        if (address == null) {
            throw new IllegalArgumentException("Invalid address");
        }

        if (client == null) {
            throw new IllegalArgumentException("Invalid client");
        }
        this.orderDateTime = orderDateTime;
        this.totalPrice = totalPrice;
        this.address = address;
        this.client = client;
        this.products = new HashSet<>();
        this.orderStatusChanges = new LinkedList<>();
        this.orderStatusChanges.add(new OrderStatusChange(OrderStatus.RECEIVED));
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean addProduct(Product product) {
        if (product == null) return false;

        return products.add(product);
    }

    public void finishOrder() {
        this.orderStatusChanges.add(new OrderStatusChange(OrderStatus.FINISHED));
    }

    public void cancelOrder() {
        this.orderStatusChanges.add(new OrderStatusChange(OrderStatus.CANCELED));
    }

    public void acceptOrder() {
        this.orderStatusChanges.add(new OrderStatusChange(OrderStatus.ACCEPTED));
    }
}
