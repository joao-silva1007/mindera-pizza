package com.mindera.pizza.services.category;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.UniqueValueViolationException;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.utils.Errors;
import com.mindera.pizza.utils.LoggingMessages;
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
            logger.info(LoggingMessages.ENTRY_ADDED_TO_DB.toString(), savedCat.getId());
            return savedCat;
        } catch (DataIntegrityViolationException e) {
            logger.error(LoggingMessages.UNIQUE_ENTRY_VIOLATION.toString(), Category.class.getSimpleName(), "name", category.getName());
            throw new UniqueValueViolationException(Category.class.getSimpleName(), "name");
        }
    }

    public Category findCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new DatabaseEntryNotFoundException(String.format(Errors.ENTRY_BY_ID_NOT_FOUND.toString(), Category.class.getSimpleName())));
        logger.info(LoggingMessages.SINGLE_ENTRY_FETCHED_FROM_DB.toString(), Category.class.getSimpleName(), categoryId);
        return category;
    }
}
