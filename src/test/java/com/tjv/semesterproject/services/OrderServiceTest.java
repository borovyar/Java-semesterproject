package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.exceptions.OrderNotExistException;
import com.tjv.semesterproject.model.OrderDto;
import com.tjv.semesterproject.repository.OrderRepository;
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
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private OrderRepository orderRepository;

    private OrderEntity order;
    private final EmployeeEntity employee;
    private final CustomerEntity customer;

    private final OrderEntity invalidOrder = new OrderEntity();

    public OrderServiceTest() {
        this.employee = new EmployeeEntity("employee", "test", (long) 10);
        this.customer = new CustomerEntity("customer", "test");
    }

    @BeforeEach
    void setUp() {
        order = new OrderEntity(customer, employee);
        order.setId((long) 1);
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(orderRepository.save(invalidOrder)).thenReturn
                (new OrderEntity(null, null));
    }

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(orderService).isNotNull();
        assertThat(customerService).isNotNull();
        assertThat(employeeService).isNotNull();
        assertThat(orderRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void registerTest(){
        this.order = orderRepository.save(order);
        Mockito.verify(orderRepository, Mockito.times(1)).save(this.order);
        assertThat(this.order.getCustomer()).isNotNull();
    }

    @Test
    @Order(3)
    public void registerInvalidTest(){
        var savedEntity = orderRepository.save(this.invalidOrder);
        Mockito.verify(orderRepository, Mockito.times(1)).save(this.invalidOrder);
        assertNull(savedEntity.getCustomer());
    }


    @Test
    @Order(4)
    public void updateTest(){
        this.order.setEmployee(new EmployeeEntity("updated", "test", (long) 20));
        this.order = orderRepository.save(this.order);

        Mockito.verify(orderRepository, Mockito.times(1)).save(this.order);

        assertEquals(this.order.getEmployee().getName(), "updated");
    }

    @Test
    @Order(5)
    public void updateInvalidTest() {
        this.order.setEmployee(new EmployeeEntity("updated", "test", (long) 20));
        this.order = orderRepository.save(this.invalidOrder);

        Mockito.verify(orderRepository, Mockito.times(1)).save(this.order);

        assertNull(order.getCustomer());
    }


    @Test
    @Order(6)
    public void getTest(){
        var savedOrder = orderRepository.save(this.order);
        var savedDtoOrder = new OrderDto((long) 1, "customer", "test",
                "employee", "test", new ArrayList<>());

        if(savedOrder.equals(order))
            Mockito.when(OrderDto.fromModel(savedOrder)).thenReturn(savedDtoOrder);

        var getResult = OrderDto.fromModel(savedOrder);
        assertEquals(getResult, savedDtoOrder);
    }

    @Test
    @Order(6)
    public void getInvalidTest(){
        var savedOrder = orderRepository.save(this.invalidOrder);

        assertNotEquals(savedOrder.getCustomer(), this.order.getCustomer());
    }


    @Test
    @Order(7)
    public void deleteTest() throws OrderNotExistException {
        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.deleteOrder(order.getId());
        Mockito.verify(orderRepository, Mockito.atLeast(1)).deleteById(order.getId());
    }

    @Test
    @Order(8)
    public void deleteInvalidTest(){
        Assertions.assertThrows(OrderNotExistException.class,
                () -> orderService.deleteOrder(order.getId()));
    }

    @Test
    @Order(9)
    public void getAllTest(){
        List<OrderEntity> orderList = new ArrayList<>();
        orderList.add(this.order);

        Mockito.when(orderRepository.findAll()).thenReturn(orderList);

        var savedOrders = orderService.readAll();
        assertThat(savedOrders.size()).isGreaterThan(0);
    }

    @Test
    @Order(10)
    public void getAllInvalidTest(){
        List<OrderEntity> orderList = new ArrayList<>();

        Mockito.when(orderRepository.findAll()).thenReturn(orderList);

        var savedOrders = orderService.readAll();
        assertFalse(savedOrders.size() > 0);
    }
}
