package com.mindera.pizza.mappers.ingredient;

import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.dto.ingredient.CreateIngredientDTO;
import lombok.experimental.UtilityClass;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@UtilityClass
public class IngredientMapper {
    private final MapperFacade mapperFacade;

    static {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        mapperFacade = factory.getMapperFacade();
    }

    public Ingredient convertToDomain(CreateIngredientDTO dto) {
        return mapperFacade.map(dto, Ingredient.class);
    }
}
