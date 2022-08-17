package com.mindera.pizza.domain.ingredient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientTest {
    @Test
    public void validIngredient() {
        Ingredient i = new Ingredient("ingredient name", 12);
        String expectedName = "ingredient name";
        int expectedStock = 12;

        assertEquals(expectedName, i.getName());
        assertEquals(expectedStock, i.getStock());
    }

    @Test
    public void invalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient("   ", 12));
    }

    @Test
    public void invalidStock() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient("ingredient name", -5));
    }
}