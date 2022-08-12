package com.mindera.pizza.domain.ingredient;

import com.mindera.pizza.domain.product.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private int stock;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "product_ingredient",
            joinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    @Getter
    private Set<Product> products;

    @Getter
    private LocalDateTime createdAt;

    @Getter @Setter
    private LocalDateTime updatedAt;

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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return stock == that.stock && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(products, that.products) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stock, products, createdAt, updatedAt);
    }
}
