package com.mindera.pizza;

import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.repositories.ingredient.IngredientRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootApplication
public class PizzaApplication {

	@Autowired
	Teste t;

	public static void main(String[] args) {
		SpringApplication.run(PizzaApplication.class, args);
	}

	@Bean
	CommandLineRunner run(CategoryRepo categoryRepo, IngredientRepo ingredientRepo, ProductRepo productRepo) {
		t.teste();
		System.out.println("123");
		return null;
	}



}
