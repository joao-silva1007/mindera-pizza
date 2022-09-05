package com.mindera.pizza.domain.ingredient;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "products")
public class Ingredient {
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

    @Getter
    @Embedded
    private final DatabaseTimestamps timestamps = new DatabaseTimestamps();

    protected Ingredient() {}

    public Ingredient(String name, int stock) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("Invalid stock");
        }

        this.name = name;
        this.stock = stock;
        this.products = new HashSet<>();
    }
}
