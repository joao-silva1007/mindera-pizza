package com.mindera.pizza.domain.ingredient;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.utils.DataValidationConstants;
import com.mindera.pizza.utils.Errors;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "products", callSuper = false)
@Builder
@AllArgsConstructor
public class Ingredient extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @Column(unique = true)
    private String name;

    @Getter @Setter
    private int stock;

    @ManyToMany(mappedBy = "ingredients", fetch = FetchType.EAGER)
    @Getter
    @Builder.Default
    private Set<Product> products = new HashSet<>();

    protected Ingredient() {}
}
