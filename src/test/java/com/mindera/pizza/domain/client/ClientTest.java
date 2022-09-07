package com.mindera.pizza.domain.client;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTest {
    static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validClient() {
        Client c = new Client("name1", "email1@gmail.com");
        String expectedName = "name1";
        String expectedEmail = "email1@gmail.com";
        String expectedPhoneNumber = "912345678";
        String expectedVatNumber = "123456789";

        c.setPhoneNumber("912345678");
        c.setVatNumber("123456789");

        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 0;

        assertEquals(expectedName, c.getName());
        assertEquals(expectedEmail, c.getEmail());
        assertEquals(expectedPhoneNumber, c.getPhoneNumber());
        assertEquals(expectedVatNumber, c.getVatNumber());
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidPhoneNumber() {
        Client c = new Client("name1", "email1@gmail.com");
        c.setPhoneNumber("758495935");
        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidVatNumber() {
        Client c = new Client("name1", "email1@gmail.com");
        c.setVatNumber("1256789");
        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidName() {
        val c = new Client("   ", "email1@gmail.com");
        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    void invalidEmail() {
        val c = new Client("name1", "email1");
        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }
}