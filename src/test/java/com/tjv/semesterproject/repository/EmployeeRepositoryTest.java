package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.EmployeeEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;
    private EmployeeEntity testEmployee;

    @Test
    @Order(1)
    void injectionTest(){
        assertThat(employeeRepository).isNotNull();
    }

    @Test
    @Order(2)
    void postEmployee(){
        this.testEmployee = this.employeeRepository.save(new EmployeeEntity("post", "test", (long) 1));
        assertThat(testEmployee).isNotNull();
        assertEquals( employeeRepository.findById(testEmployee.getId()).get().getName(), "post");
    }

    @Test
    @Order(3)
    void updateEmployee(){
        this.testEmployee = this.employeeRepository.save(new EmployeeEntity("update", "test", (long) 1));
        assertThat(testEmployee).isNotNull();
        testEmployee.setName("testUpdate");
        assertEquals(employeeRepository.save(testEmployee).getName(), "testUpdate");
    }

    @Test
    @Order(4)
    void getEmployee(){
        this.testEmployee = this.employeeRepository.save(new EmployeeEntity("get", "test", (long) 1));
        assertThat(testEmployee).isNotNull();
        assertEquals(employeeRepository.findById(testEmployee.getId()).get().getName(), "get");
    }

    @Test
    @Order(5)
    void deleteEmployee(){
        this.testEmployee = this.employeeRepository.save(new EmployeeEntity("delete", "test", (long) 1));
        assertThat(testEmployee).isNotNull();
        employeeRepository.deleteById(testEmployee.getId());
        assertTrue(employeeRepository.findById(testEmployee.getId()).isEmpty());
    }
}
