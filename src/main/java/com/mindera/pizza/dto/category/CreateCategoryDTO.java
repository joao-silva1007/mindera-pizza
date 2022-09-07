package com.mindera.pizza.dto.category;

import lombok.Builder;

public record CreateCategoryDTO(String name) {
    @Builder public CreateCategoryDTO {}
}
