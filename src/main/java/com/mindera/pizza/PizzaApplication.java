package com.mindera.pizza;
import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import com.mindera.pizza.services.order.RestaurantOrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaApplication.class, args);
	}


	@Bean
	CommandLineRunner run(RestaurantOrderService restaurantOrderService, ClientRepo clientRepo, AddressRepo addressRepo, RestaurantOrderRepo restaurantOrderRepo, ProductRepo productRepo, CategoryRepo categoryRepo) {
		return args -> {
			Client c = new Client("name1", "email@gmail.com");
			c.setVatNumber("123456789");
			Client savedC = clientRepo.save(c);
			Address a = new Address("str1", 10, "1234-123", "city", "house", savedC);
			addressRepo.save(a);
			Category cat = new Category("cat1");
			Category savedCat = categoryRepo.save(cat);
			Product p = new Product("prod1", 20f, 10, savedCat);
			Product savedP = productRepo.save(p);
		};
	}

}
