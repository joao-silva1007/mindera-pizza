package com.mindera.pizza.domain.category;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {
    static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validCategory() {
        Category c = new Category("name");
        String expectedName = "name";
        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 0;

        assertEquals(expectedName, c.getName());
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }

    @Test
    public void invalidName() {
        val c = new Category("   ");
        int validationErrorAmount = validator.validate(c).size();
        int expectedErrorAmount = 1;
        assertEquals(expectedErrorAmount, validationErrorAmount);
    }
}