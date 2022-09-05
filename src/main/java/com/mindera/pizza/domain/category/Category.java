package com.mindera.pizza.domain.category;

import com.mindera.pizza.domain.DatabaseTimestamps;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@EqualsAndHashCode
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter
    @Embedded
    private final DatabaseTimestamps timestamps = new DatabaseTimestamps();

    protected Category() {}

    public Category(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        this.name = name;
    }
}
