package com.mindera.pizza.domain.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {
    @Test
    public void validCategory() {
        Category c = new Category("name");
        String expectedName = "name";

        assertEquals(expectedName, c.getName());
    }

    @Test
    public void invalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Category("   "));
    }
}