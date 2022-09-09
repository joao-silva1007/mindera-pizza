package com.mindera.pizza.domain.product;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.utils.DataValidationConstants;
import com.mindera.pizza.utils.Errors;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = {"ingredients", "restaurantOrders"}, callSuper = false)
@Builder
@AllArgsConstructor
public class Product extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @NotBlank(message = DataValidationConstants.INVALID_NAME)
    private String name;

    @Getter @Setter
    @Positive(message = DataValidationConstants.INVALID_PRICE)
    private float price;

    @Getter @Setter
    @Positive(message = DataValidationConstants.INVALID_STOCK)
    private int stock;

    @ManyToOne
    @Getter @Setter
    @NotNull(message = DataValidationConstants.INVALID_CATEGORY)
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_ingredient",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    )
    @Getter
    @Builder.Default
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    private Set<RestaurantOrder> restaurantOrders;

    protected Product() {}

    public boolean addIngredient(Ingredient ingredient) {
        if (ingredient == null) return false;
        return this.ingredients.add(ingredient);
    }
}
