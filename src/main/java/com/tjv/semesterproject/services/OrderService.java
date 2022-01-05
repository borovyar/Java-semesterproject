package com.tjv.semesterproject.services;

import com.tjv.semesterproject.controller.CustomerController;
import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.entity.ProductEntity;
import com.tjv.semesterproject.exceptions.*;
import com.tjv.semesterproject.model.CustomerDto;
import com.tjv.semesterproject.model.OrderDto;
import com.tjv.semesterproject.model.OrderDtoRegistrate;
import com.tjv.semesterproject.repository.CustomerRepository;
import com.tjv.semesterproject.repository.EmployeeRepository;
import com.tjv.semesterproject.repository.OrderRepository;
import com.tjv.semesterproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private CustomerController customerController;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public OrderDto registerOrder(OrderDtoRegistrate orderDtoRegistrate) throws EmployeeNotExistException,
            ProductNotExistException, CustomerExistException {
        customerController.registerCustomer(OrderDtoRegistrate.customerDto(orderDtoRegistrate));
        var customer = customerRepository.findByNameAndSurname(orderDtoRegistrate.getCustomer_name(),
                orderDtoRegistrate.getCustomer_surname());

        if(employeeRepository.findById(orderDtoRegistrate.getEmployee_id()).isEmpty())
            throw new EmployeeNotExistException("employee does not exist");
        EmployeeEntity employee = employeeRepository.findById(orderDtoRegistrate.getEmployee_id()).get();

        List<ProductEntity> products = new ArrayList<>();

        OrderEntity order = new OrderEntity(customer, employee);

        createProductList(orderDtoRegistrate, order, products);
        order.setProducts(products);

        return OrderDto.fromModel(orderRepository.save(order));
    }

    public OrderDto updateOrder(OrderDtoRegistrate orderDto, Long id) throws OrderNotExistException,
            CustomerNotExistException, EmployeeNotExistException, ProductNotExistException {
        if (orderRepository.findById(id).isEmpty())
            throw new OrderNotExistException("Order Doesn't Exist");

        OrderEntity order = orderRepository.findById(id).get();

        if(customerRepository.findByNameAndSurname(orderDto.getCustomer_name(),
                orderDto.getCustomer_surname()) == null )
            throw new CustomerNotExistException("Customer does not exist");
        order.setCustomer(customerRepository.findByNameAndSurname(orderDto.getCustomer_name(),
                orderDto.getCustomer_surname()));

        if(employeeRepository.findById(orderDto.getEmployee_id()).isEmpty())
            throw new EmployeeNotExistException("employee does not exist");
        order.setEmployee(employeeRepository.findById(orderDto.getEmployee_id()).get());

        ArrayList<ProductEntity> products = new ArrayList<>();
        createProductList(orderDto, order, products);

        order.setUpdatedProducts(products);
        return OrderDto.fromModel(orderRepository.save(order));
    }

    private void createProductList(OrderDtoRegistrate orderDto, OrderEntity order, List<ProductEntity> products)
            throws ProductNotExistException {
        for (String product_id : orderDto.getProducts_id()) {
            if( productRepository.findById(product_id).isEmpty() )
                throw new ProductNotExistException("product does not exist");
            ProductEntity product = productRepository.findById(product_id).get();
            product.addOrder(order);
            products.add(product);
        }
    }

    public OrderDto getOrder(Long id) throws OrderNotExistException {
        if (orderRepository.findById(id).isEmpty())
            throw new OrderNotExistException("Order does not exist!");
        return OrderDto.fromModel(orderRepository.findById(id).get());
    }

    public void deleteOrder(Long id) throws OrderNotExistException {
        if(orderRepository.findById(id).isEmpty())
            throw new OrderNotExistException("order does not exist");
        for(ProductEntity product : orderRepository.findById(id).get().getProducts())
            product.removeOrder(orderRepository.findById(id).get());
        orderRepository.deleteById(id);
    }

    public Collection<OrderDto> readAll() {
        Set<OrderDto> orders = new HashSet<>();
        for (OrderEntity order   : orderRepository.findAll())
            orders.add(OrderDto.fromModel(order));
        return orders;
    }
}
