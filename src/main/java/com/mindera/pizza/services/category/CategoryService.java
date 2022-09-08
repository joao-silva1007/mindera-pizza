package com.mindera.pizza.services.category;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.UniqueValueViolationException;
import com.mindera.pizza.mappers.category.CategoryMapper;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.utils.Errors;
import com.mindera.pizza.utils.LoggingMessages;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    private final CategoryRepo categoryRepo;

    public Category createCategory(CreateCategoryDTO categoryDTO) {
        Category category = CategoryMapper.convertToDomain(categoryDTO);

        try {
            Category savedCat = categoryRepo.save(category);
            logger.info(LoggingMessages.ENTRY_ADDED_TO_DB.toString(), savedCat.getId());
            return savedCat;
        } catch (DataIntegrityViolationException e) {
            logger.error(LoggingMessages.UNIQUE_ENTRY_VIOLATION.toString(), Category.class.getSimpleName(), "name", category.getName());
            throw new UniqueValueViolationException(Category.class.getSimpleName(), "name");
        }
    }

    public List<Category> getCategories(String categoryName) {
        List<Category> categories;
        if (Objects.isNull(categoryName)) {
            categories = categoryRepo.findAll();
        } else {
            categories = categoryRepo.getCategoriesByNameContains(categoryName);
        }
        logger.info(LoggingMessages.ENTRIES_FETCHED_FROM_DB.toString(), categories.size(), Category.class.getSimpleName());
        return categories;
    }

    public Category findCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new DatabaseEntryNotFoundException(Errors.ENTRY_BY_ID_NOT_FOUND, Category.class.getSimpleName()));
        logger.info(LoggingMessages.SINGLE_ENTRY_FETCHED_FROM_DB.toString(), Category.class.getSimpleName(), categoryId);
        return category;
    }
}
