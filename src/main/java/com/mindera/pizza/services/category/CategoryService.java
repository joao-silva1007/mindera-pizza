package com.mindera.pizza.services.category;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.exceptions.GlobalExceptionHandler;
import com.mindera.pizza.repositories.category.CategoryRepo;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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
            return categoryRepo.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A Category already exists with the inserted name");
        }
    }

    public List<Category> getCategories() {
        List<Category> categories = categoryRepo.findAll();
        logger.info("Fetched {} Categories from the DB", categories.size());
        return categories;
    }
}
