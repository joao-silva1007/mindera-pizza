package com.mindera.pizza.dto.ingredient;

import com.mindera.pizza.utils.DataValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIngredientDTO {
    @NotBlank(message = DataValidationConstants.INVALID_NAME)
    String name;
    @Positive(message = DataValidationConstants.INVALID_STOCK)
    int stock;
}
