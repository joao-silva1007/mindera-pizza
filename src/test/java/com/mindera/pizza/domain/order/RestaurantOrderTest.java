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
        c = Client.builder().name("name1").email("email1@gmail.com").build();
        a = Address.builder().streetName("street name").streetNumber(25).zipCode("1234-123").city("city").nickname("house").client(c).build();
        Category cat = new Category("name");
        p = Product.builder().name("product1").stock(5).price(10.2f).category(cat).build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validRestaurantOrder() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(c).build();

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
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now().plusDays(4)).address(a).client(c).build();
        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidAddress() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(null).client(c).build();
        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidClient() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(null).build();
        int validationErrorAmount = validator.validate(ro).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void nullProduct() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(c).build();
        assertFalse(ro.addProduct(null));
    }

    @Test
    public void productThatAlreadyExists() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(c).build();
        ro.addProduct(p);

        assertFalse(ro.addProduct(p));
    }

    @Test
    public void finishOrder() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(c).build();
        ro.acceptOrder();
        ro.finishOrder();

        assertEquals(OrderStatus.FINISHED, ro.getCurrentStatus());
    }

    @Test
    public void cancelOrder() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(c).build();
        ro.cancelOrder();

        assertEquals(OrderStatus.CANCELED, ro.getCurrentStatus());
    }

    @Test
    public void acceptOrder() {
        val ro = RestaurantOrder.builder().orderDateTime(LocalDateTime.now()).address(a).client(c).build();
        ro.acceptOrder();

        assertEquals(OrderStatus.ACCEPTED, ro.getCurrentStatus());
    }
}