package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    private CustomerEntity customerEntity;

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(customerRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void postTest(){
        this.customerEntity = customerRepository.save(new CustomerEntity("post", "test" ));
        assertThat(customerEntity).isNotNull();
        assertEquals(customerRepository.findById(customerEntity.getId()).get().getName(), "post");
    }

    @Test
    @Order(3)
    public void updateTest(){
        this.customerEntity = customerRepository.save(new CustomerEntity("update", "test" ));
        assertThat(customerEntity).isNotNull();
        customerEntity.setName("testUpdate");
        assertEquals(customerRepository.save(customerEntity).getName(), "testUpdate");
    }

    @Test
    @Order(4)
    public void getTest(){
        this.customerEntity = customerRepository.save(new CustomerEntity("get", "test" ));
        assertThat(customerEntity).isNotNull();
        assertEquals(customerRepository.findById(customerEntity.getId()).get().getName(), "get");
    }

    @Test
    @Order(5)
    public void deleteTest(){
        this.customerEntity = customerRepository.save(new CustomerEntity("delete", "test" ));
        assertThat(customerEntity).isNotNull();
        customerRepository.deleteById(customerEntity.getId());
        assertTrue(customerRepository.findById(customerEntity.getId()).isEmpty());
    }

}