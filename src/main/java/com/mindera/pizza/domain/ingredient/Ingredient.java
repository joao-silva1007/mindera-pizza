package com.mindera.pizza.domain.ingredient;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.utils.Errors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "products", callSuper = false)
public class Ingredient extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private int stock;

    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.EAGER)
    @Getter
    private Set<Product> products;

    protected Ingredient() {}

    public Ingredient(String name, int stock) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_NAME.toString());
        }

        if (stock < 0) {
            throw new IllegalArgumentException(Errors.INVALID_STOCK.toString());
        }

        this.name = name;
        this.stock = stock;
        this.products = new HashSet<>();
    }
}
