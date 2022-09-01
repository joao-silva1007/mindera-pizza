package com.mindera.pizza.controllers.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PizzaApplication.class)
@AutoConfigureMockMvc
public class RestaurantOrderControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantOrderRepo restaurantOrderRepo;

    static RestaurantOrder ro1;
    static RestaurantOrder ro2;
    static RestaurantOrder ro3;

    @BeforeAll
    public static void beforeAll(@Autowired RestaurantOrderRepo restaurantOrderRepo, @Autowired AddressRepo addressRepo, @Autowired ClientRepo clientRepo, @Autowired ProductRepo productRepo) {
        Address a1 = addressRepo.findById(1L).orElseThrow();
        Address a2 = addressRepo.findById(2L).orElseThrow();
        Client c1 = clientRepo.findById(1L).orElseThrow();
        Client c2 = clientRepo.findById(2L).orElseThrow();
        Product p = productRepo.findById(1L).orElseThrow();

        ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10), a1, c1);
        ro1.addProduct(p);
        ro1 = restaurantOrderRepo.save(ro1);

        ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10), a2, c2);
        ro2.cancelOrder();
        ro2.addProduct(p);
        ro2 = restaurantOrderRepo.save(ro2);

        ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10), a2, c2);
        ro3.addProduct(p);
        ro3 = restaurantOrderRepo.save(ro3);
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
        restaurantOrderRepo.deleteById(4L);
    }

    @Test
    public void addOrderWithInvalidClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 10L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Client not found in the database"))
                .andReturn();
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
    }

    @Test
    public void addOrderWithInvalidAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 10L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Address not found in the database"))
                .andReturn();
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
    }

    @Test
    public void getAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[3]").doesNotExist())
                .andReturn();
    }

    @Test
    public void getAllOrdersFilteredByClientName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("clientName", "name2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.name").value("name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].client.name").value("name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].client.name").doesNotExist())
                .andReturn();
    }

    @Test
    public void getAllOrdersFilteredByClientVatNumber() throws Exception {
        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("vatNumber", "123456789"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.vatNumber").value("123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").doesNotExist())
                .andReturn();
    }

    @Test
    public void getAllOrdersFilteredByClientPhoneNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("phoneNumber", "912345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.phoneNumber").value("912345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").doesNotExist())
                .andReturn();
    }

    @Test
    public void getAllOrdersFilteredByClientPhoneNumberAndClientName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("phoneNumber", "912345678")
                        .queryParam("clientName", "name1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.phoneNumber").value("912345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.name").value("name1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").doesNotExist())
                .andReturn();
    }

    @Test
    public void getAllOrdersFilteredByOrderStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("currentStatus", "RECEIVED"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currentStatus").value("RECEIVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currentStatus").value("RECEIVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]").doesNotExist())
                .andReturn();
    }
}
