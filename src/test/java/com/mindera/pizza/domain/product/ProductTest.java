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
        i = Ingredient.builder().name("ing1").stock(20).build();
    }

    @Test
    public void validProduct() {
        val p = Product.builder().name("product1").price(10.2f).stock(5).category(c).build();
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
        val p = Product.builder().name("   ").price(10.2f).stock(5).category(c).build();
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidPrice() {
        val p = Product.builder().name("prod1").price(-10.2f).stock(5).category(c).build();
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidStock() {
        val p = Product.builder().name("prod1").price(10.2f).stock(-5).category(c).build();
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidCategory() {
        val p = Product.builder().name("prod1").price(10.2f).stock(5).category(null).build();
        int validationErrorAmount = validator.validate(p).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void nullIngredient() {
        Category c = new Category("name");
        val p = Product.builder().name("prod1").price(10.2f).stock(5).category(c).build();
        assertFalse(p.addIngredient(null));
    }

    @Test
    public void ingredientThatAlreadyExists() {
        Category c = new Category("name");
        val p = Product.builder().name("prod1").price(10.2f).stock(5).category(c).build();
        p.addIngredient(i);

        assertFalse(p.addIngredient(i));
    }
}