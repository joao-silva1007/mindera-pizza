package com.mindera.pizza.controllers.ingredient;

import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.dto.ingredient.CreateIngredientDTO;
import com.mindera.pizza.services.ingredient.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
@AllArgsConstructor
public class IngredientController {
    private IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Ingredient> createIngredient(@RequestBody CreateIngredientDTO ingredientDTO) {
        return new ResponseEntity<>(ingredientService.createIngredient(ingredientDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        return new ResponseEntity<>(ingredientService.findIngredientById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getIngredients(@RequestParam(required = false) String ingredientName) {
        return new ResponseEntity<>(ingredientService.getIngredients(ingredientName), HttpStatus.OK);
    }
}
