package com.mindera.pizza.services.category;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.GlobalExceptionHandler;
import com.mindera.pizza.exceptions.UniqueValueViolationException;
import com.mindera.pizza.repositories.category.CategoryRepo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    private final CategoryRepo categoryRepo;

    public Category createCategory(CreateCategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.name())
                .build();

        try {
            Category savedCat = categoryRepo.save(category);
            logger.info("Added a new category with id {} to the DB", savedCat.getId());
            return savedCat;
        } catch (DataIntegrityViolationException e) {
            logger.error("Attempted to create a category with an existing name ({})", category.getName());
            throw new UniqueValueViolationException(Category.class.getSimpleName(), "name");
        }
    }

    public Category findCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new DatabaseEntryNotFoundException("Category not found with the specified Id"));
        logger.info("Fetched Category with id {}", categoryId);
        return category;
    }
}
