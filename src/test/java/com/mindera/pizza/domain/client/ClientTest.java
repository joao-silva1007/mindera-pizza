package com.mindera.pizza.domain.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClientTest {

    @Test
    void validClient() {
        Client c = new Client("name1", "email1@gmail.com");
        String expectedName = "name1";
        String expectedEmail = "email1@gmail.com";
        String expectedPhoneNumber = "912345678";
        String expectedVatNumber = "123456789";

        c.setPhoneNumber("912345678");
        c.setVatNumber("123456789");

        assertEquals(expectedName, c.getName());
        assertEquals(expectedEmail, c.getEmail());
        assertEquals(expectedPhoneNumber, c.getPhoneNumber());
        assertEquals(expectedVatNumber, c.getVatNumber());
    }

    @Test
    void invalidPhoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            c.setPhoneNumber("758495935");
        });
    }

    @Test
    void invalidVatNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            c.setVatNumber("1256789");
        });
    }

    @Test
    void invalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Client("   ", "email1@gmail.com"));
    }

    @Test
    void invalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Client("name1", "email1"));
    }
}