package com.mindera.pizza.domain.address;

import com.mindera.pizza.domain.client.Client;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    static Client c;
    static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        c = new Client("name1", "email1@gmail.com");
    }

    @Test
    public void validAddress() {
        Client c = new Client("name1", "email1@gmail.com");
        Address a = new Address("street name", 25, "1243-123", "city", "house", c);
        a.setApartmentInformation("floor 3 apartment 2");

        String expectedStreetName = "street name";
        int expectedStreetNumber = 25;
        String expectedZipCode = "1243-123";
        String expectedCity = "city";
        String expectedNickname = "house";
        String expectedApartmentInformation = "floor 3 apartment 2";

        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 0;

        assertNotNull(a.getClient());
        assertEquals(expectedStreetName, a.getStreetName());
        assertEquals(expectedStreetNumber, a.getStreetNumber());
        assertEquals(expectedZipCode, a.getZipCode());
        assertEquals(expectedCity, a.getCity());
        assertEquals(expectedNickname, a.getNickname());
        assertEquals(expectedApartmentInformation, a.getApartmentInformation());
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidStreetName() {
        val a = new Address("", 25, "1243-123", "city", "house", c);
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidStreetNumber() {
        val a = new Address("street name", -25, "1243-123", "city", "house", c);
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidZipCode() {
        val a = new Address("street name", 25, "12423-123", "city", "house", c);
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidCity() {
        val a = new Address("street name", 25, "1223-123", "   ", "house", c);
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidNickname() {
        val a = new Address("street name", 25, "1243-123", "city", "", c);
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidApartmentInformation() {
        val a = new Address("street name", 25, "1243-123", "city", "house", c);
        a.setApartmentInformation("    ");
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }
}