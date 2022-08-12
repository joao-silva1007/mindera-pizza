package com.mindera.pizza;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.repositories.ingredient.IngredientRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class Teste {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    IngredientRepo ingredientRepo;

    @Autowired
    ProductRepo productRepo;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public void teste() {
        entityManager.getTransaction().begin();
        Category c = new Category("cat1");
        Category c2 = categoryRepo.saveAndFlush(c);

        Ingredient i = new Ingredient("ing1", 10);
        Ingredient i3 = ingredientRepo.saveAndFlush(i);

        Product p = new Product("prod1", 12.4f, 10, c2);
        //Ingredient i2 = ingredientRepo.findById(1L).get();
        p.addIngredient(i3);
        productRepo.saveAndFlush(p);
        entityManager.getTransaction().commit();

        entityManager.close();

        Optional<Product> p2 = productRepo.findById(1L);
        if (p2.isPresent()) {
            System.out.println("");
        }
        System.out.println("");
    }
}
