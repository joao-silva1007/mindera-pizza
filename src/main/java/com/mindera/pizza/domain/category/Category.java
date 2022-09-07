package com.mindera.pizza.domain.category;

import lombok.Builder;
import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.utils.Errors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
public class Category extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @Column(unique = true)
    private String name;

    protected Category() {}

    @Builder
    public Category(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_NAME.toString());
        }

        this.name = name;
    }
}
