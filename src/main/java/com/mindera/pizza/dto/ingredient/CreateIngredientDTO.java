package com.mindera.pizza.dto.ingredient;

import lombok.Builder;

public record CreateIngredientDTO(String name, int stock) {
    @Builder public CreateIngredientDTO {}
}
