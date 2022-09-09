package com.mindera.pizza.mappers.category;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import lombok.experimental.UtilityClass;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@UtilityClass
public class CategoryMapper {
    private final MapperFacade mapperFacade;

    static {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(CreateCategoryDTO.class, Category.class)
                .byDefault()
                .register();
        mapperFacade = factory.getMapperFacade();
    }

    public Category convertToDomain(CreateCategoryDTO dto) {
        return mapperFacade.map(dto, Category.class);
    }
}
