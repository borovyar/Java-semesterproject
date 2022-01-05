package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.exceptions.CustomerNotExistException;
import com.tjv.semesterproject.model.CustomerDto;
import com.tjv.semesterproject.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    @MockBean
    private CustomerRepository customerRepository;

    private CustomerEntity customer;
    private final CustomerEntity invalidCustomer = new CustomerEntity();

    @BeforeEach
    void setUp() {
        customer = new CustomerEntity("spring", "test");
        customer.setId((long) 1);
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);
        Mockito.when(customerRepository.save(invalidCustomer)).thenReturn
                (new CustomerEntity(null, null));
    }

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(customerService).isNotNull();
        assertThat(customerRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void registerTest(){
        this.customer = customerRepository.save(customer);
        Mockito.verify(customerRepository, Mockito.times(1)).save(this.customer);
        assertThat(this.customer.getName()).isNotNull();
    }

    @Test
    @Order(3)
    public void registerInvalidTest(){
        var savedEntity = customerRepository.save(new CustomerEntity());
        Mockito.verify(customerRepository, Mockito.times(1)).save(new CustomerEntity());
        assertNull(savedEntity.getName());
    }


    @Test
    @Order(4)
    public void updateTest(){
        this.customer.setName("updated");
        this.customer = customerRepository.save(this.customer);

        Mockito.verify(customerRepository, Mockito.times(1)).save(this.customer);

        assertEquals(this.customer.getName(), "updated");
    }

    @Test
    @Order(5)
    public void updateInvalidTest() {
        this.customer = customerRepository.save(this.invalidCustomer);

        Mockito.verify(customerRepository, Mockito.times(1)).save(this.customer);

        assertNull(customer.getName());
    }


    @Test
    @Order(6)
    public void getTest(){
        var savedCustomer = customerRepository.save(this.customer);
        var savedDtoCustomer = new CustomerDto((long) 1, "spring", "test");

        if(savedCustomer.equals(customer))
            Mockito.when(CustomerDto.fromModel(savedCustomer)).thenReturn
                    (new CustomerDto(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getSurname()));

        var getResult = CustomerDto.fromModel(savedCustomer);
        assertEquals(getResult, savedDtoCustomer);
    }

    @Test
    @Order(6)
    public void getInvalidTest(){
        var savedCustomer = customerRepository.save(this.invalidCustomer);
        var savedDtoCustomer = new CustomerDto((long) 1, "spring", "test");

        if(savedCustomer.equals(customer))
            Mockito.when(CustomerDto.fromModel(savedCustomer)).thenReturn
                    (new CustomerDto(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getSurname()));

        var getResult = CustomerDto.fromModel(savedCustomer);
        assertNotEquals(getResult, savedDtoCustomer);
    }


    @Test
    @Order(7)
    public void deleteTest() throws CustomerNotExistException {
        Mockito.when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(customer.getId());
        Mockito.verify(customerRepository, Mockito.atLeast(1)).deleteById(customer.getId());
    }

    @Test
    @Order(8)
    public void deleteInvalidTest(){
        Assertions.assertThrows(CustomerNotExistException.class,
                () -> customerService.deleteCustomer(customer.getId()));
    }

    @Test
    @Order(9)
    public void getAllTest(){
        List<CustomerEntity> customerList = new ArrayList<>();
        customerList.add(this.customer);

        Mockito.when(customerRepository.findAll()).thenReturn(customerList);

        var savedCustomers = customerService.readAll();
        assertThat(savedCustomers.size()).isGreaterThan(0);
    }

    @Test
    @Order(10)
    public void getAllInvalidTest(){
        List<CustomerEntity> customerList = new ArrayList<>();

        Mockito.when(customerRepository.findAll()).thenReturn(customerList);

        var savedCustomers = customerService.readAll();
        assertFalse(savedCustomers.size() > 0);
    }
}
