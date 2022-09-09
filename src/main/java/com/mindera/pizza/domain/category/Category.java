package com.mindera.pizza.domain.category;

import com.mindera.pizza.utils.DataValidationConstants;
import lombok.*;
import com.mindera.pizza.domain.DatabaseTimestamps;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class Category extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @Column(unique = true)
    private String name;

    protected Category() {}

    public Category(String name) {
        this.name = name;
    }
}
