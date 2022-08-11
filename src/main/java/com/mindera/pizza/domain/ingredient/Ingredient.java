package com.mindera.pizza.domain.ingredient;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private int stock;

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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
