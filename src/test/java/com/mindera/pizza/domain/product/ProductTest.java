package com.mindera.pizza.domain.product;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.ingredient.Ingredient;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    static Validator validator;
    static Category c;
    static Ingredient i;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        c = new Category("name");
        i = new Ingredient("ing1", 20);
    }

    @Test
    public void validProduct() {
        Product p = new Product("product1", 10.2f, 5, c);
        String expectedName = "product1";
        float expectedPrice = 10.2f;
        int expectedStock = 5;

        int validationErrorAmount = validator.validate(i).size();
        int expectedErrorAmount = 0;

        assertEquals(expectedName, p.getName());
        assertEquals(expectedPrice, p.getPrice());
        assertEquals(expectedStock, p.getStock());
        assertNotNull(p.getCategory());
        assertTrue(p.addIngredient(i));
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidName() {
        val p = new Product("   ", 10.2f, 5, c);
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidPrice() {
        val p = new Product("prod1", -10.2f, 5, c);
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidStock() {
        val p = new Product("prod1", 10.2f, -5, c);
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidCategory() {
        val p = new Product("prod1", 10.2f, 5, null);
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
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