package com.mindera.pizza.services.ingredient;

import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.dto.ingredient.CreateIngredientDTO;
import com.mindera.pizza.exceptions.DatabaseEntryNotFoundException;
import com.mindera.pizza.exceptions.UniqueValueViolationException;
import com.mindera.pizza.mappers.ingredient.IngredientMapper;
import com.mindera.pizza.repositories.ingredient.IngredientRepo;
import com.mindera.pizza.utils.Errors;
import com.mindera.pizza.utils.LoggingMessages;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class IngredientService {
    private static final Logger logger = LogManager.getLogger(IngredientService.class);

    private final IngredientRepo ingredientRepo;

    public Ingredient createIngredient(CreateIngredientDTO ingredientDTO) {
        Ingredient ingredient = IngredientMapper.convertToDomain(ingredientDTO);

        try {
            Ingredient savedIngredient = ingredientRepo.save(ingredient);
            logger.info(LoggingMessages.ENTRY_ADDED_TO_DB.toString(), Ingredient.class.getSimpleName(), savedIngredient.getId());
            return savedIngredient;
        } catch (DataIntegrityViolationException e) {
            logger.error(LoggingMessages.UNIQUE_ENTRY_VIOLATION.toString(), Ingredient.class.getSimpleName(), "name", ingredient.getName());
            throw new UniqueValueViolationException(Ingredient.class.getSimpleName(), "name");
        }
    }

    public Ingredient findIngredientById(Long ingredientId) {
        return ingredientRepo.findById(ingredientId)
                .orElseThrow(() -> new DatabaseEntryNotFoundException(Errors.ENTRY_BY_ID_NOT_FOUND, Ingredient.class.getSimpleName()));
    }

    public List<Ingredient> getIngredients(String ingredientName) {
        List<Ingredient> ingredients;
        if (Objects.isNull(ingredientName)) {
            ingredients = ingredientRepo.findAll();
        } else {
            ingredients = ingredientRepo.getIngredientsByNameContains(ingredientName);
        }
        logger.info(LoggingMessages.ENTRIES_FETCHED_FROM_DB.toString(), ingredients.size(), Ingredient.class.getSimpleName());
        return ingredients;
    }
}
