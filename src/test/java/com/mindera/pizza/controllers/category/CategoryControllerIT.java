package com.mindera.pizza.controllers.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.domain.category.Category;
import com.mindera.pizza.dto.category.CreateCategoryDTO;
import com.mindera.pizza.repositories.category.CategoryRepo;
import org.junit.jupiter.api.BeforeAll;
import com.mindera.pizza.utils.Errors;
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
                        .content(mapper.writeValueAsString(new CreateCategoryDTO("drinks"))))
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
                        .content(mapper.writeValueAsString(new CreateCategoryDTO(""))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Errors.INVALID_NAME.toString()));
    }

    @Test
    public void addCategoryWithExistingName() throws Exception {
        categoryRepo.save(new Category("cat123"));
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/category")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateCategoryDTO("cat123"))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
                        .value(String.format(Errors.UNIQUE_VALUE_VIOLATION.toString(), Category.class.getSimpleName(), "name")));
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
