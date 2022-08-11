package com.mindera.pizza.domain.address;

import com.mindera.pizza.domain.client.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
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

        assertNotNull(a.getClient());
        assertEquals(expectedStreetName, a.getStreetName());
        assertEquals(expectedStreetNumber, a.getStreetNumber());
        assertEquals(expectedZipCode, a.getZipCode());
        assertEquals(expectedCity, a.getCity());
        assertEquals(expectedNickname, a.getNickname());
        assertEquals(expectedApartmentInformation, a.getApartmentInformation());
    }

    @Test
    void invalidStreetName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            new Address("", 25, "1243-123", "city", "house", c);
        });
    }

    @Test
    void invalidStreetNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            new Address("street name", -25, "1243-123", "city", "house", c);
        });
    }

    @Test
    void invalidZipCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            new Address("street name", 25, "12423-123", "city", "house", c);
        });
    }

    @Test
    void invalidCity() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            new Address("street name", 25, "1243-123", "   ", "house", c);
        });
    }

    @Test
    void invalidNickname() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            new Address("street name", 25, "1243-123", "city", "", c);
        });
    }

    @Test
    void invalidApartmentInformation() {
        assertThrows(IllegalArgumentException.class, () -> {
            Client c = new Client("name1", "email1@gmail.com");
            Address a = new Address("street name", 25, "1243-123", "city", "house", c);
            a.setApartmentInformation("    ");
        });
    }
}