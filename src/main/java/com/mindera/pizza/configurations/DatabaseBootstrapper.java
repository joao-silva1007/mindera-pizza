package com.mindera.pizza.configurations;

import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//TODO: Remove after all endpoints are implemented
@Component
public class DatabaseBootstrapper {
    @Bean
    CommandLineRunner run(ClientRepo clientRepo, AddressRepo addressRepo, ProductRepo productRepo, CategoryRepo categoryRepo) {
        return args -> {
            Client c = new Client("name1", "email@gmail.com");
            c.setVatNumber("123456789");
            c.setPhoneNumber("912345678");

            Client c2 = new Client("name2", "email@gmail.com");
            c2.setVatNumber("123658789");
            c2.setPhoneNumber("912327578");

            Client savedC = clientRepo.save(c);
            Client savedC2 = clientRepo.save(c2);

            Address a = new Address("str1", 10, "1234-123", "city", "house", savedC);
            Address a2 = new Address("str27", 98, "1234-578", "city", "house", savedC2);
            addressRepo.save(a);
            addressRepo.save(a2);
            Category cat = new Category("cat1");
            Category savedCat = categoryRepo.save(cat);
            Product p = new Product("prod1", 20f, 10, savedCat);
            Product savedP = productRepo.save(p);
        };
    }
}
