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
        c = Client.builder().name("name1").email("email1@gmail.com").build();
    }

    @Test
    public void validAddress() {
        val a = Address.builder().streetName("street name").streetNumber(25).zipCode("1243-123").city("city").nickname("house").client(c).build();
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
        val a = Address.builder().streetName("").streetNumber(25).zipCode("1234-123").city("city").nickname("house").client(c).build();
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidStreetNumber() {
        val a = Address.builder().streetName("street name").streetNumber(-25).zipCode("1234-123").city("city").nickname("house").client(c).build();
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidZipCode() {
        val a = Address.builder().streetName("street name").streetNumber(25).zipCode("12334-123").city("city").nickname("house").client(c).build();
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidCity() {
        val a = Address.builder().streetName("street name").streetNumber(25).zipCode("1234-123").city("   ").nickname("house").client(c).build();
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidNickname() {
        val a = Address.builder().streetName("street name").streetNumber(25).zipCode("1234-123").city("city").nickname("").client(c).build();
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidApartmentInformation() {
        val a = Address.builder().streetName("street name").streetNumber(25).zipCode("1234-123").city("city").nickname("house").client(c).build();
        a.setApartmentInformation("    ");
        int validationErrorAmount = validator.validate(a).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }
}