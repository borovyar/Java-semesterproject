package com.tjv.semesterproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.model.EmployeeDto;
import com.tjv.semesterproject.model.OrderDto;
import com.tjv.semesterproject.model.OrderDtoRegistrate;
import com.tjv.semesterproject.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc orderMvc;
    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper;
    private OrderEntity order;
    private OrderDto orderDto;
    private OrderDtoRegistrate orderDtoRegistrate;

    @BeforeEach
    public void setUp(){
        var tmpCustomer = new CustomerEntity("web", "test");
        var tmpEmployee = new EmployeeEntity("web", "test", 1L);
        this.orderDtoRegistrate =  new OrderDtoRegistrate("web", "test",
                1L, new ArrayList<>());
        this.order = new OrderEntity(tmpCustomer, tmpEmployee);
        this.order.setId(1L);
        this.orderDto = OrderDto.fromModel(order);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void addOrderTest() throws Exception {
        Mockito.when(orderService.registerOrder(orderDtoRegistrate)).
                thenReturn(orderDto);

        orderMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderDtoRegistrate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer_name", is(orderDto.getCustomer_name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer_surname", is(orderDto.getCustomer_surname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_name", is(orderDto.getEmployee_name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_surname", is(orderDto.getEmployee_surname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", is(orderDto.getProducts())));
    }

    @Test
    @Order(2)
    public void addOrder400Test() throws Exception {
        orderMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(3)
    public void updateOrderTest() throws Exception {
        var updatedOrder = new OrderDto(1L, "web", "test",
                "web", "test", new ArrayList<>());
        var updatedRegister = new OrderDtoRegistrate("web", "test",
                1L, new ArrayList<>());

        Mockito.when(orderService.updateOrder(updatedRegister, order.getId())).
                thenReturn(updatedOrder);

        orderMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedRegister)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(updatedOrder.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer_name", is(updatedOrder.getCustomer_name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer_surname", is(updatedOrder.getCustomer_surname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_name", is(updatedOrder.getEmployee_name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_surname", is(updatedOrder.getEmployee_surname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", is(updatedOrder.getProducts())));
    }

    @Test
    @Order(4)
    public void updateOrder400Test() throws Exception {
        orderMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("update")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
    public void getOrderTest() throws Exception {
        Mockito.when(orderService.getOrder(order.getId())).
                thenReturn(orderDto);

        orderMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(orderDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer_name", is(orderDto.getCustomer_name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer_surname", is(orderDto.getCustomer_surname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_name", is(orderDto.getEmployee_name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee_surname", is(orderDto.getEmployee_surname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.products", is(orderDto.getProducts())));
    }

    @Test
    @Order(6)
    public void getOrder404Test() throws Exception {
        orderMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", "2L"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(7)
    public void deleteOrderTest() throws Exception {
        var response = orderMvc.perform(MockMvcRequestBuilders
                        .delete("/orders/{id}", order.getId()))
                .andReturn();

        assertEquals(response.getResponse().getStatus(), 200);
        assertEquals(response.getResponse().getContentAsString(),
                "order with id " + order.getId() + " has been deleted");
    }

    @Test
    @Order(8)
    public void deleteOrder404Test() throws Exception {
        orderMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", "2L"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
