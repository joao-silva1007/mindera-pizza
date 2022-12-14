package com.mindera.pizza.controllers.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.repositories.category.CategoryRepo;
import com.mindera.pizza.utils.DataValidationConstants;
import org.junit.jupiter.api.BeforeAll;
import com.mindera.pizza.utils.Errors;
import lombok.val;
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

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PizzaApplication.class)
@AutoConfigureMockMvc
public class CategoryControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepo categoryRepo;

    @BeforeAll
    static void beforeAll(@Autowired CategoryRepo categoryRepo) {
        Category cat = new Category("Toppings");
        categoryRepo.save(cat);
    }

    @Test
    public void addCategory() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateCategoryDTO
                                .builder()
                                .name("drinks")
                                .build())))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn();
        int id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");
        categoryRepo.deleteById((long) id);
    }

    @Test
    public void addInvalidCategory() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateCategoryDTO
                                .builder()
                                .name("")
                                .build())))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(DataValidationConstants.INVALID_NAME));
    }

    @Test
    public void addCategoryWithExistingName() throws Exception {
        val cat = new Category("cat123");
        Long id = categoryRepo.save(cat).getId();
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(CreateCategoryDTO
                                .builder()
                                .name("cat123")
                                .build())))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                        .value(String.format(Errors.UNIQUE_VALUE_VIOLATION.toString(), Category.class.getSimpleName(), "name")));
        categoryRepo.deleteById(id);
    }

    @Test
    public void getCategories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2]").doesNotExist());
    }

    @Test
    public void getCategoriesByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category")
                        .queryParam("categoryName", "Top"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Toppings"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1]").doesNotExist());
    }

    @Test
    public void findExistingCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Toppings"));
    }

    @Test
    public void findNonExistingCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/15555"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                        .value(String.format(Errors.ENTRY_BY_ID_NOT_FOUND.toString(), Category.class.getSimpleName())));
    }
}
