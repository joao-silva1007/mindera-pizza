package com.mindera.pizza.repositories.category;

import com.mindera.pizza.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> getCategoriesByNameContains(String name);
}
