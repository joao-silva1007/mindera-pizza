package com.mindera.pizza.domain.product;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.ingredient.Ingredient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    public void validProduct() {
        Category c = new Category("name");
        Product p = new Product("product1", 10.2f, 5, c);
        Ingredient i = new Ingredient("ing1", 20);
        String expectedName = "product1";
        float expectedPrice = 10.2f;
        int expectedStock = 5;

        assertEquals(expectedName, p.getName());
        assertEquals(expectedPrice, p.getPrice());
        assertEquals(expectedStock, p.getStock());
        assertNotNull(p.getCategory());
        assertTrue(p.addIngredient(i));
    }

    @Test
    public void invalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category c = new Category("cat1");
            new Product("   ", 10.2f, 5, c);
        });
    }

    @Test
    public void invalidPrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category c = new Category("cat1");
            new Product("prod1", -10.2f, 5, c);
        });
    }

    @Test
    public void invalidStock() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category c = new Category("cat1");
            new Product("prod1", 10.2f, -5, c);
        });
    }

    @Test
    public void invalidCategory() {
        assertThrows(IllegalArgumentException.class, () -> new Product("prod1", 10.2f, 5, null));
    }

    @Test
    public void nullIngredient() {
        Category c = new Category("name");
        Product p = new Product("product1", 10.2f, 5, c);
        assertFalse(p.addIngredient(null));
    }

    @Test
    public void ingredientThatAlreadyExists() {
        Category c = new Category("name");
        Product p = new Product("product1", 10.2f, 5, c);
        Ingredient i = new Ingredient("ing1", 20);
        p.addIngredient(i);

        assertFalse(p.addIngredient(i));
    }
}