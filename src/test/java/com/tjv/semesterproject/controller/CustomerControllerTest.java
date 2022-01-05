package com.tjv.semesterproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.model.CustomerDto;
import com.tjv.semesterproject.services.CustomerService;
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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc customerMvc;
    @MockBean
    private CustomerService customerService;

    private ObjectMapper objectMapper;
    private CustomerEntity customer;
    private CustomerDto customerDto;

    @BeforeEach
    public void setUp(){
        this.customer = new CustomerEntity("web", "test");
        this.customer.setId(1L);
        this.customerDto = CustomerDto.fromModel(customer);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void addCustomerTest() throws Exception {
        Mockito.when(customerService.registerCustomer(customerDto)).
                thenReturn(customerDto);

        customerMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(customer.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(customer.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is(customer.getSurname())));
    }

    @Test
    @Order(2)
    public void addCustomer400Test() throws Exception {
        customerMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(3)
    public void updateCustomerTest() throws Exception {
        var updatedCustomer = new CustomerDto(1L, "update", "test");
        Mockito.when(customerService.updateCustomer(customerDto, customer.getId())).
                thenReturn(updatedCustomer);

        customerMvc.perform(MockMvcRequestBuilders.put("/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(updatedCustomer.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(updatedCustomer.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is(updatedCustomer.getSurname())));
    }

    @Test
    @Order(4)
    public void updateCustomer400Test() throws Exception {
        customerMvc.perform(MockMvcRequestBuilders.put("/customers/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("update")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
    public void getCustomerTest() throws Exception {
        Mockito.when(customerService.getCustomer(customer.getId())).
                thenReturn(customerDto);

        customerMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(customerDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(customerDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is(customerDto.getSurname())));
    }

    @Test
    @Order(6)
    public void getCustomer404Test() throws Exception {
        customerMvc.perform(MockMvcRequestBuilders.get("/customers/{id}", "2L"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(7)
    public void deleteCustomerTest() throws Exception {
        var response = customerMvc.perform(MockMvcRequestBuilders
                        .delete("/customers/{id}", customer.getId()))
                .andReturn();

        assertEquals(response.getResponse().getStatus(), 200);
        assertEquals(response.getResponse().getContentAsString(),
                "Customer with id:" + customer.getId() + " has been deleted");
    }

    @Test
    @Order(8)
    public void deleteCustomer404Test() throws Exception {
        customerMvc.perform(MockMvcRequestBuilders.delete("/customers/{id}", "2L"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
