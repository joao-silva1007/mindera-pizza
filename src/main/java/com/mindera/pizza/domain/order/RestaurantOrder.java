package com.mindera.pizza.domain.order;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.exceptions.InvalidStatusChangeException;
import com.mindera.pizza.utils.DataValidationConstants;
import com.mindera.pizza.utils.Errors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = {"products"}, callSuper = false)
public class RestaurantOrder extends DatabaseTimestamps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @Past(message = DataValidationConstants.INVALID_ORDER_DATE_TIME)
    private LocalDateTime orderDateTime;

    @Getter
    private float totalPrice;

    @ManyToOne
    @Getter @Setter
    @NotNull(message = DataValidationConstants.INVALID_ADDRESS)
    private Address address;

    @ManyToOne
    @Getter @Setter
    @NotNull(message = DataValidationConstants.INVALID_CLIENT)
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

    protected RestaurantOrder() {}

    @Builder
    public RestaurantOrder(LocalDateTime orderDateTime, Address address, Client client) {
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
            throw new InvalidStatusChangeException(Errors.ILLEGAL_STATUS_CHANGE, this.currentStatus, OrderStatus.FINISHED);
        }
        changeStatus(OrderStatus.FINISHED);
    }

    public void cancelOrder() {
        if (!List.of(OrderStatus.RECEIVED, OrderStatus.ACCEPTED).contains(this.currentStatus)) {
            throw new InvalidStatusChangeException(Errors.ILLEGAL_STATUS_CHANGE, this.currentStatus, OrderStatus.CANCELED);
        }
        changeStatus(OrderStatus.CANCELED);
    }

    public void acceptOrder() {
        if (this.currentStatus != OrderStatus.RECEIVED) {
            throw new InvalidStatusChangeException(Errors.ILLEGAL_STATUS_CHANGE, this.currentStatus, OrderStatus.ACCEPTED);
        }
        changeStatus(OrderStatus.ACCEPTED);
    }

    private void changeStatus(OrderStatus newStatus) {
        this.currentStatus = newStatus;
        this.orderStatusChanges.add(new OrderStatusChange(newStatus));
    }
}
