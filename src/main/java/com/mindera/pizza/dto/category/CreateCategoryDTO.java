package com.mindera.pizza.dto.category;

import com.mindera.pizza.utils.DataValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryDTO {
    @NotBlank(message = DataValidationConstants.INVALID_NAME)
    String name;
}
