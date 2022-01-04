package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    private OrderEntity orderEntity;

    private OrderEntity setOrderTest( String customerName, String employeeName ){
        var tmpEmployee = employeeRepository.save(new EmployeeEntity(employeeName, employeeName, (long) 1));
        var tmpCustomer = customerRepository.save( new CustomerEntity(customerName, customerName));
        return this.orderRepository.save(new OrderEntity(tmpCustomer, tmpEmployee));
    }

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(orderRepository).isNotNull();
        assertThat(customerRepository).isNotNull();
        assertThat(employeeRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void postTest(){
        this.orderEntity = setOrderTest("postCustomer", "postEmployee");
        assertThat(orderEntity).isNotNull();
        assertEquals(orderRepository.findById(orderEntity.getId()).get().getCustomer().getName(),
                "postCustomer");
    }

    private CustomerEntity updateCustomer (String customerName){
        return customerRepository.save(new CustomerEntity(customerName, customerName));
    }

    @Test
    @Order(3)
    public void updateTest(){
        this.orderEntity = setOrderTest("updateCustomer", "updateEmployee");
        assertThat(orderEntity).isNotNull();
        orderEntity.setCustomer(updateCustomer("testUpdate"));
        assertEquals(orderRepository.save(orderEntity).getCustomer().getName(), "testUpdate");
    }

    @Test
    @Order(4)
    public void getTest(){
        this.orderEntity = setOrderTest("getCustomer", "getEmployee");
        assertThat(orderEntity).isNotNull();
        assertEquals(orderRepository.findById(orderEntity.getId()).get().getEmployee().getName(),
                "getEmployee");
    }

    @Test
    @Order(5)
    public void deleteTest(){
        this.orderEntity = setOrderTest("deleteCustomer", "deleteEmployee");
        assertThat(orderEntity).isNotNull();
        orderRepository.deleteById(orderEntity.getId());
        assertTrue(orderRepository.findById(orderEntity.getId()).isEmpty());
    }
}
