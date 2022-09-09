package com.mindera.pizza.dto.order;

import com.mindera.pizza.utils.DataValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRestaurantOrderDTO {
    @NotBlank(message = DataValidationConstants.INVALID_ORDER_DATE_TIME)
    String orderDateTime;

    List<Long> productIds;

    @Positive(message = DataValidationConstants.INVALID_ADDRESS)
    Long addressId;

    @Positive(message = DataValidationConstants.INVALID_CLIENT)
    Long clientId;
}
