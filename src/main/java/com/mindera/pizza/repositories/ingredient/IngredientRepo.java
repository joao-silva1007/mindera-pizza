package com.mindera.pizza.repositories.ingredient;

import com.mindera.pizza.domain.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
    List<Ingredient> getIngredientsByNameContains(String name);
}
