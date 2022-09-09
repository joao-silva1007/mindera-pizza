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
        val i = Ingredient.builder().name("ingredient name").stock(12).build();
        String expectedName = "ingredient name";
        int expectedStock = 12;

        int validationErrorAmount = validator.validate(i).size();
        int expectedErrorAmount = 0;

        assertEquals(expectedName, i.getName());
        assertEquals(expectedStock, i.getStock());
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }
}