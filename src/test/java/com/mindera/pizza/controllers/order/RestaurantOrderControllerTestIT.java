package com.mindera.pizza.controllers.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PizzaApplication.class)
@AutoConfigureMockMvc
public class RestaurantOrderControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantOrderRepo restaurantOrderRepo;

    @BeforeEach
    public void beforeEach() {
        restaurantOrderRepo.deleteAll();
    }

    @Test
    public void addOrder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 1L))))
            .andExpect(MockMvcResultMatchers.status().is(201))
            .andReturn();
        assertFalse(restaurantOrderRepo.findAll().isEmpty());
    }

    @Test
    public void addOrderWithInvalidClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 2L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Client not found in the database"))
                .andReturn();
        assertTrue(restaurantOrderRepo.findAll().isEmpty());
    }

    @Test
    public void addOrderWithInvalidProduct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(2L), 1L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Products not found in the database"))
                .andReturn();
        assertTrue(restaurantOrderRepo.findAll().isEmpty());
    }

    @Test
    public void addOrderWithInvalidAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 2L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Address not found in the database"))
                .andReturn();
        assertTrue(restaurantOrderRepo.findAll().isEmpty());
    }

    @Test
    public void addOrderWithInvalidOrderDateTime() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2023-04-10 10:10:10", List.of(1L), 1L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid order date time"))
                .andReturn();
        assertTrue(restaurantOrderRepo.findAll().isEmpty());
    }
}
