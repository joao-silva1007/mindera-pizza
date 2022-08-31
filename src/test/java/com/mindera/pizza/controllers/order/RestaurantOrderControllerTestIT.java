package com.mindera.pizza.controllers.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private AddressRepo addressRepo;

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
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 10L))))
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
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 10L, 1L))))
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

    @Test
    public void getAllOrders() throws Exception {
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro1.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro2.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro3.addProduct(productRepo.findById(1L).orElseThrow());

        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

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
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro1.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro2.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro3.addProduct(productRepo.findById(1L).orElseThrow());

        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

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
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro1.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro2.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro3.addProduct(productRepo.findById(1L).orElseThrow());

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
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro1.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro2.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro3.addProduct(productRepo.findById(1L).orElseThrow());

        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

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
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro1.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro2.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro3.addProduct(productRepo.findById(1L).orElseThrow());

        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

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
        RestaurantOrder ro1 = new RestaurantOrder(LocalDateTime.of(2022,4,10,10,10,10),addressRepo.findById(1L).orElseThrow(), clientRepo.findById(1L).orElseThrow());
        ro1.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro2 = new RestaurantOrder(LocalDateTime.of(2022,3,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro2.cancelOrder();
        ro2.addProduct(productRepo.findById(1L).orElseThrow());
        RestaurantOrder ro3 = new RestaurantOrder(LocalDateTime.of(2022,2,10,10,10,10),addressRepo.findById(2L).orElseThrow(), clientRepo.findById(2L).orElseThrow());
        ro3.addProduct(productRepo.findById(1L).orElseThrow());

        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

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
