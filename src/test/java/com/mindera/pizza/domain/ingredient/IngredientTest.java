package com.mindera.pizza.domain.ingredient;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IngredientTest {
    static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validIngredient() {
        Ingredient i = new Ingredient("ingredient name", 12);
        String expectedName = "ingredient name";
        int expectedStock = 12;

        int validationErrorAmount = validator.validate(i).size();
        int expectedErrorAmount = 0;

        assertEquals(expectedName, i.getName());
        assertEquals(expectedStock, i.getStock());
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidName() {
        val aaa = new Ingredient("    ", 12);
        int validationErrorAmount = validator.validate(aaa).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidStock() {
        val aaa = new Ingredient("name", -12);
        int validationErrorAmount = validator.validate(aaa).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }
}