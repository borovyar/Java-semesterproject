package com.tjv.semesterproject.controller;

import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.model.EmployeeDto;
import com.tjv.semesterproject.services.EmployeeService;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc employeeMvc;
    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;
    private EmployeeEntity employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    public void setUp(){
        this.employee = new EmployeeEntity("web", "test", (long) 1);
        this.employee.setId((long) 1);
        this.employeeDto = EmployeeDto.fromModel(employee);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void addEmployeeTest() throws Exception {
        Mockito.when(employeeService.registerEmployee(employeeDto)).
                thenReturn(employeeDto);

        employeeMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(employee.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(employee.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is(employee.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary", is(employee.getSalary().intValue())));
    }

    @Test
    @Order(2)
    public void addEmployee400Test() throws Exception {
        employeeMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(3)
    public void updateEmployeeTest() throws Exception {
        var updatedEmployee = new EmployeeDto(1L, "update", "test", 1L);
        Mockito.when(employeeService.updateEmployee(employeeDto, employee.getId())).
                thenReturn(updatedEmployee);

        employeeMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(updatedEmployee.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(updatedEmployee.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is(updatedEmployee.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary", is(updatedEmployee.getSalary().intValue())));
    }

    @Test
    @Order(4)
    public void updateEmployee400Test() throws Exception {
        employeeMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("update")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
    public void getEmployeeTest() throws Exception {
        Mockito.when(employeeService.getEmployee(employee.getId())).
                thenReturn(employeeDto);

        employeeMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(employeeDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(employeeDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", is(employeeDto.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary", is(employeeDto.getSalary().intValue())));
    }

    @Test
    @Order(6)
    public void getEmployee404Test() throws Exception {
        employeeMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", "2L"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(7)
    public void deleteEmployeeTest() throws Exception {
        var response = employeeMvc.perform(MockMvcRequestBuilders
                        .delete("/employees/{id}", employee.getId()))
                .andReturn();

        assertEquals(response.getResponse().getStatus(), 200);
        assertEquals(response.getResponse().getContentAsString(),
                "Employee with id:" + employee.getId() + " has been deleted");
    }

    @Test
    @Order(8)
    public void deleteEmployee404Test() throws Exception {
        employeeMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", "2L"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
