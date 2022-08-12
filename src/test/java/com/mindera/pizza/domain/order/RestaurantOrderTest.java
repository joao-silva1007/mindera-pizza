package com.mindera.pizza.domain.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.domain.product.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantOrderTest {
    @Test
    public void validRestaurantOrder() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        Category cat = new Category("name");
        Product p = new Product("product1", 10.2f, 5, cat);
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), 10.2f, a, c);

        float expectedPrice = 10.2f;
        assertEquals(expectedPrice, ro.getTotalPrice());
        assertNotNull(ro.getClient());
        assertNotNull(ro.getAddress());
        assertTrue(ro.addProduct(p));
        assertEquals(1, ro.getOrderStatusChanges().size());
    }

    @Test
    public void invalidOrderDateTime() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            Address a = new Address("street name", 25, "1243-123", "city", "house", c);
            new RestaurantOrder(LocalDateTime.now().plusDays(4), 10.2f, a, c);
        });
    }

    @Test
    public void invalidPrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            Address a = new Address("street name", 25, "1243-123", "city", "house", c);
            new RestaurantOrder(LocalDateTime.now(), -10.2f, a, c);
        });
    }

    @Test
    public void invalidAddress() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            new RestaurantOrder(LocalDateTime.now(), 10.2f, null, c);
        });
    }

    @Test
    public void invalidClient() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            Address a = new Address("street name", 25, "1243-123", "city", "house", c);
            new RestaurantOrder(LocalDateTime.now(), 10.2f, a, null);
        });
    }

    @Test
    public void nullProduct() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), 10.2f, a, c);
        assertFalse(ro.addProduct(null));
    }

    @Test
    public void productThatAlreadyExists() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), 10.2f, a, c);
        Category cat = new Category("name");
        Product p = new Product("product1", 10.2f, 5, cat);
        ro.addProduct(p);

        assertFalse(ro.addProduct(p));
    }

    @Test
    public void finishOrder() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        Category cat = new Category("name");
        Product p = new Product("product1", 10.2f, 5, cat);
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), 10.2f, a, c);
        ro.finishOrder();

        assertEquals(OrderStatus.FINISHED, ro.getOrderStatusChanges().get(1).getOrderStatus());
    }

    @Test
    public void cancelOrder() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        Category cat = new Category("name");
        Product p = new Product("product1", 10.2f, 5, cat);
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), 10.2f, a, c);
        ro.cancelOrder();

        assertEquals(OrderStatus.CANCELED, ro.getOrderStatusChanges().get(1).getOrderStatus());
    }

    @Test
    public void acceptOrder() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        Category cat = new Category("name");
        Product p = new Product("product1", 10.2f, 5, cat);
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), 10.2f, a, c);
        ro.acceptOrder();

        assertEquals(OrderStatus.ACCEPTED, ro.getOrderStatusChanges().get(1).getOrderStatus());
    }
}