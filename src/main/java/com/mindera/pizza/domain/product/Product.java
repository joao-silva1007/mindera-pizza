package com.mindera.pizza.domain.product;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.domain.order.RestaurantOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = {"ingredients", "restaurantOrders"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private float price;

    @Getter @Setter
    private int stock;

    @ManyToOne
    @Getter @Setter
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_ingredient",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    )
    @Getter
    private Set<Ingredient> ingredients;

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    private Set<RestaurantOrder> restaurantOrders;

    @Getter
    @Embedded
    private final DatabaseTimestamps timestamps = new DatabaseTimestamps();

    protected Product() {}

    public Product(String name, float price, int stock, Category category) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Invalid price");
        }

        if (stock < 0 ) {
            throw new IllegalArgumentException("Invalid stock");
        }

        if (category == null) {
            throw new IllegalArgumentException("Invalid category");
        }
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.ingredients = new HashSet<>();
    }

    public boolean addIngredient(Ingredient ingredient) {
        if (ingredient == null) return false;
        return this.ingredients.add(ingredient);
    }
}
