package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.exceptions.CustomerExistException;
import com.tjv.semesterproject.exceptions.CustomerNotExistException;
import com.tjv.semesterproject.exceptions.OrderNotExistException;
import com.tjv.semesterproject.model.CustomerAllOrdersDto;
import com.tjv.semesterproject.model.CustomerDto;
import com.tjv.semesterproject.model.CustomerOrderDto;
import com.tjv.semesterproject.repository.CustomerRepository;
import com.tjv.semesterproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public CustomerDto registerCustomer(CustomerDto customerDto) throws CustomerExistException {
        var customer = CustomerDto.toModel(customerDto);
        if (customerRepository.findByNameAndSurname(customer.getName(), customer.getSurname()) != null)
            throw new CustomerExistException("Customer Already Exist");
        return CustomerDto.fromModel(customerRepository.save(customer));
    }

    public void updateCustomer(CustomerDto customerDto, Long id) throws CustomerNotExistException {
        if (customerRepository.findById(id).isEmpty())
            throw new CustomerNotExistException("Customer doesn't Exist!");
        CustomerEntity customer = customerRepository.findById(id).get();
        customer.setName(customerDto.getName());
        customer.setSurname(customerDto.getSurname());
        customerRepository.save(customer);
    }

    public CustomerDto getCustomer(Long id) throws CustomerNotExistException {
        if (customerRepository.findById(id).isEmpty())
            throw new CustomerNotExistException("Customer does not exist!");
        return CustomerDto.fromModel(customerRepository.findById(id).get());
    }

    public CustomerOrderDto getCustomerOrder(Long customer_id, Integer order_id) throws CustomerNotExistException, OrderNotExistException {
        if (customerRepository.findById(customer_id).isEmpty())
            throw new CustomerNotExistException("Customer does not exist!");
        CustomerEntity customer = customerRepository.findById(customer_id).get();
        if (customer.getOrders().size() < order_id)
            throw new OrderNotExistException("Order does not exist!");
        OrderEntity order = customer.getOrders().get(order_id);
        return CustomerOrderDto.toModel(order);
    }

    public void deleteCustomer(Long id) throws CustomerNotExistException {
        if(!customerRepository.existsById(id))
            throw new CustomerNotExistException("Customer does not exist!");
        customerRepository.deleteById(id);
    }

    public Collection<CustomerDto> readAll() {
        ArrayList<CustomerDto> customersDto = new ArrayList<>();
        for (CustomerEntity customer : customerRepository.findAll())
            customersDto.add(CustomerDto.fromModel(customer));
        return customersDto;
    }

    public CustomerAllOrdersDto readAllOrders(Long customer_id) throws CustomerNotExistException {
        if (customerRepository.findById(customer_id).isEmpty())
            throw new CustomerNotExistException("Customer does not exist!");
        return CustomerAllOrdersDto.toAllModel(customerRepository.findById(customer_id).get());
    }
}
