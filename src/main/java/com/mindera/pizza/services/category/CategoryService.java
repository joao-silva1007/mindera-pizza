package com.mindera.pizza.services.category;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.repositories.category.CategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepo categoryRepo;

    public Category createCategory(CreateCategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.name())
                .build();

        try {
            return categoryRepo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A Category already exists with the inserted name");
        }
    }
}
