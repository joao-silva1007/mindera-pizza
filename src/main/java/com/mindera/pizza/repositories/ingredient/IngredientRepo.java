package com.mindera.pizza.repositories.ingredient;

import com.mindera.pizza.domain.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
}
