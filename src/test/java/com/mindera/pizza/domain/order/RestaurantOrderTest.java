package com.mindera.pizza.domain.order;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.product.Product;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantOrderTest {
    static Validator validator;
    static Client c;
    static Address a;
    static Product p;

    @BeforeAll
    static void beforeAll() {
        c = new Client("name1", "email1@gmail.com");
        a = new Address("street name", 25, "1243-123", "city", "house", c);
        Category cat = new Category("name");
        p = new Product("product1", 10.2f, 5, cat);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validRestaurantOrder() {
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), a, c);

        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 0;

        float expectedPrice = 10.2f;
        assertTrue(ro.addProduct(p));
        assertEquals(expectedPrice, ro.getTotalPrice());
        assertNotNull(ro.getClient());
        assertNotNull(ro.getAddress());
        assertEquals(1, ro.getOrderStatusChanges().size());
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidOrderDateTime() {
        val ro = new RestaurantOrder(LocalDateTime.now().plusDays(4), a, c);
        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidAddress() {
        val ro = new RestaurantOrder(LocalDateTime.now(), null, c);
        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidClient() {
        val ro = new RestaurantOrder(LocalDateTime.now(), a, null);
        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void nullProduct() {
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), a, c);
        assertFalse(ro.addProduct(null));
    }

    @Test
    public void productThatAlreadyExists() {
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), a, c);
        ro.addProduct(p);

        assertFalse(ro.addProduct(p));
    }

    @Test
    public void finishOrder() {
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), a, c);
        ro.acceptOrder();
        ro.finishOrder();

        assertEquals(OrderStatus.FINISHED, ro.getCurrentStatus());
    }

    @Test
    public void cancelOrder() {
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), a, c);
        ro.cancelOrder();

        assertEquals(OrderStatus.CANCELED, ro.getCurrentStatus());
    }

    @Test
    public void acceptOrder() {
        RestaurantOrder ro = new RestaurantOrder(LocalDateTime.now(), a, c);
        ro.acceptOrder();

        assertEquals(OrderStatus.ACCEPTED, ro.getCurrentStatus());
    }
}