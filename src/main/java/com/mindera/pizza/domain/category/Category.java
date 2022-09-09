package com.mindera.pizza.domain.category;

import com.mindera.pizza.utils.DataValidationConstants;
import lombok.Builder;
import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.utils.Errors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
        this.name = name;
    }
}
