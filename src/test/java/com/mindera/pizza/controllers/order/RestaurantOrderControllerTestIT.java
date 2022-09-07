package com.mindera.pizza.controllers.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mindera.pizza.PizzaApplication;
import com.mindera.pizza.domain.address.Address;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.domain.order.OrderStatus;
import com.mindera.pizza.domain.order.RestaurantOrder;
import com.mindera.pizza.domain.product.Product;
import com.mindera.pizza.dto.order.CreateRestaurantOrderDTO;
import com.mindera.pizza.dto.order.UpdateRestaurantOrderStatusDTO;
import com.mindera.pizza.repositories.address.AddressRepo;
import com.mindera.pizza.repositories.client.ClientRepo;
import com.mindera.pizza.repositories.order.RestaurantOrderRepo;
import com.mindera.pizza.repositories.product.ProductRepo;
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

    static Address a1;
    static Address a2;
    static Client c1;
    static Client c2;
    static Product p;

    @BeforeAll
    public static void beforeAll(@Autowired RestaurantOrderRepo restaurantOrderRepo, @Autowired AddressRepo addressRepo, @Autowired ClientRepo clientRepo, @Autowired ProductRepo productRepo) {
        a1 = addressRepo.findById(1L).orElseThrow();
        a2 = addressRepo.findById(2L).orElseThrow();
        c1 = clientRepo.findById(1L).orElseThrow();
        c2 = clientRepo.findById(2L).orElseThrow();
        p = productRepo.findById(1L).orElseThrow();

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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 1L))))
            .andExpect(MockMvcResultMatchers.status().is(201))
            .andReturn();
        int id = JsonPath.parse(result.getResponse().getContentAsString()).read("$.id");
        restaurantOrderRepo.deleteById((long) id);
    }

    @Test
    public void addOrderWithInvalidClient() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 1L, 10L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(String.format(Errors.ENTRY_NOT_FOUND.toString(), Client.class.getSimpleName())));
    }

    @Test
    public void addOrderWithInvalidProduct() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(2L), 1L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(String.format(Errors.ENTRY_NOT_FOUND.toString(), Product.class.getSimpleName())));
    }

    @Test
    public void addOrderWithInvalidAddress() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2022-04-10 10:10:10", List.of(1L), 10L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(String.format(Errors.ENTRY_NOT_FOUND.toString(), Address.class.getSimpleName())));
    }

    @Test
    public void addOrderWithInvalidOrderDateTime() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CreateRestaurantOrderDTO("2023-04-10 10:10:10", List.of(1L), 1L, 1L))))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(Errors.INVALID_ORDER_DATE_TIME.toString()));
    }

    @Test
    public void getAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[3]").doesNotExist());
    }

    @Test
    public void getAllOrdersFilteredByClientName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("clientName", "name2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.name").value("name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].client.name").value("name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].client.name").doesNotExist());
    }

    @Test
    public void getAllOrdersFilteredByClientVatNumber() throws Exception {
        restaurantOrderRepo.saveAll(List.of(ro1, ro2, ro3));

        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("vatNumber", "123456789"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.vatNumber").value("123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").doesNotExist());
    }

    @Test
    public void getAllOrdersFilteredByClientPhoneNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("phoneNumber", "912345678"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].client.phoneNumber").value("912345678"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").doesNotExist());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").doesNotExist());
    }

    @Test
    public void getAllOrdersFilteredByOrderStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order")
                        .queryParam("currentStatus", "RECEIVED"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currentStatus").value("RECEIVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currentStatus").value("RECEIVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]").doesNotExist());
    }

    @Test
    public void findExistingOrderById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDateTime").value("2022-04-10T10:10:10"));

    }

    @Test
    public void findNonExisingOrderById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order/500"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(String.format(Errors.ENTRY_BY_ID_NOT_FOUND.toString(), RestaurantOrder.class.getSimpleName())));
    }

    @Test
    public void changeOrderStatus() throws Exception {
        val ro4 = new RestaurantOrder(LocalDateTime.of(2022,1,10,10,10,10), a1, c1);
        val savedRO4 = restaurantOrderRepo.save(ro4);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.patch("/order/" + savedRO4.getId() + "/status")
                        .content(mapper.writeValueAsString(new UpdateRestaurantOrderStatusDTO(OrderStatus.ACCEPTED)))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentStatus").value("ACCEPTED"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/order/" + savedRO4.getId() + "/status")
                        .content(mapper.writeValueAsString(new UpdateRestaurantOrderStatusDTO(OrderStatus.FINISHED)))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentStatus").value("FINISHED"));
        restaurantOrderRepo.deleteById(savedRO4.getId());
    }

    @Test
    public void changeOrderStatusToCanceled() throws Exception {
        val ro4 = new RestaurantOrder(LocalDateTime.of(2022,1,10,10,10,10), a1, c1);
        val savedRO4 = restaurantOrderRepo.save(ro4);
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.patch("/order/" + savedRO4.getId() + "/status")
                        .content(mapper.writeValueAsString(new UpdateRestaurantOrderStatusDTO(OrderStatus.CANCELED)))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentStatus").value("CANCELED"));
        restaurantOrderRepo.deleteById(savedRO4.getId());
    }

    @Test
    void changeToInvalidOrderStatus() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.patch("/order/1/status")
                        .content(mapper.writeValueAsString(new UpdateRestaurantOrderStatusDTO(OrderStatus.RECEIVED)))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value(String.format(Errors.ILLEGAL_STATUS_CHANGE_TO.toString(), OrderStatus.RECEIVED)));
    }
}
