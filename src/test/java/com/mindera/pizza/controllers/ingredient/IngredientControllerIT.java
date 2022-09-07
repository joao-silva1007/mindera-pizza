package com.mindera.pizza.controllers.ingredient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.domain.ingredient.Ingredient;
import com.mindera.pizza.dto.ingredient.CreateIngredientDTO;
import com.mindera.pizza.repositories.ingredient.IngredientRepo;
import com.mindera.pizza.utils.DataValidationConstants;
import com.mindera.pizza.utils.Errors;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PizzaApplication.class)
@AutoConfigureMockMvc
public class IngredientControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IngredientRepo ingredientRepo;

    @BeforeAll
    static void beforeAll(@Autowired IngredientRepo ingredientRepo) {
        val ing = new Ingredient("Cheese", 25);
        val ing2 = new Ingredient("Bacon", 50);
        ingredientRepo.saveAll(List.of(ing, ing2));
    }

    @AfterAll
    static void afterAll(@Autowired IngredientRepo ingredientRepo) {
        ingredientRepo.deleteAll();
    }

    @Test
    public void addIngredient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/ingredient")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateIngredientDTO("Pepperoni", 10))))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn();
        int id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");
        ingredientRepo.deleteById((long) id);
    }

    @Test
    public void addInvalidIngredient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/ingredient")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateIngredientDTO("", 10))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(DataValidationConstants.INVALID_NAME));
    }

    @Test
    public void addIngredientWithExistingName() throws Exception {
        val ing = new Ingredient("Ham", 30);
        Long id = ingredientRepo.save(ing).getId();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/ingredient")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateIngredientDTO("Ham", 5))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                        .value(String.format(Errors.UNIQUE_VALUE_VIOLATION.toString(), Ingredient.class.getSimpleName(), "name")));
        ingredientRepo.deleteById(id);
    }

    @Test
    public void getIngredients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2]").doesNotExist());
    }

    @Test
    public void getIngredientsByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredient")
                        .queryParam("ingredientName", "Bacon"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Bacon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1]").doesNotExist());
    }

    @Test
    public void findExistingIngredientById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredient/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Cheese"));
    }

    @Test
    public void findNonExistingIngredientById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredient/15555"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                        .value(String.format(Errors.ENTRY_BY_ID_NOT_FOUND.toString(), Ingredient.class.getSimpleName())));
    }
}
