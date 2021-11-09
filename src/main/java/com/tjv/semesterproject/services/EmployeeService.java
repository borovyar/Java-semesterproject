package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.exceptions.CustomerNotExistException;
import com.tjv.semesterproject.exceptions.EmployeeExistException;
import com.tjv.semesterproject.exceptions.EmployeeNotExistException;
import com.tjv.semesterproject.exceptions.OrderNotExistException;
import com.tjv.semesterproject.model.EmployeeAllOrdersDto;
import com.tjv.semesterproject.model.EmployeeDto;
import com.tjv.semesterproject.model.EmployeeOrderDto;
import com.tjv.semesterproject.repository.EmployeeRepository;
import com.tjv.semesterproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public void registerEmployee(EmployeeEntity employee) throws EmployeeExistException {
        if (employeeRepository.findByNameAndSurname(employee.getName(), employee.getSurname()) != null)
            throw new EmployeeExistException("Employee Already Exists");
        employeeRepository.save(employee);
    }

    public void updateEmployee(EmployeeDto employeeDto, Long id) throws EmployeeNotExistException {
        if ( employeeRepository.findById(id).isEmpty())
            throw new EmployeeNotExistException("Employee doesn't Exist!");
        EmployeeEntity employee = employeeRepository.findById(id).get();
        employee.setName(employeeDto.getName());
        employee.setSurname(employeeDto.getSurname());
        employee.setSalary(employeeDto.getSalary());
        employeeRepository.save(employee);
    }

    public EmployeeDto getEmployee(Long id) throws EmployeeNotExistException {
        if (employeeRepository.findById(id).isEmpty())
            throw new EmployeeNotExistException("Employee does not exist!");
        return EmployeeDto.toModel(employeeRepository.findById(id).get());
    }

    public EmployeeOrderDto getEmployeeOrder(Long employee_id, Integer order_id) throws OrderNotExistException, EmployeeNotExistException {
        if (employeeRepository.findById(employee_id).isPresent())
            throw new EmployeeNotExistException("employee does not exist");
        EmployeeEntity employee = employeeRepository.findById(employee_id).get();
        if( employee.getOrders().size() < order_id )
            throw new OrderNotExistException("order does not exist");
        return EmployeeOrderDto.toOrderModel(employee.getOrders().get(order_id));
    }

    public void deleteEmployee(Long id) throws EmployeeNotExistException {
        if( employeeRepository.findById(id).isEmpty())
            throw new EmployeeNotExistException("employee does not exist");
        employeeRepository.deleteById(id);
    }

    public Collection<EmployeeDto> readAll() {
        ArrayList<EmployeeDto> employees = new ArrayList<>();
        for (EmployeeEntity employee : employeeRepository.findAll())
            employees.add(EmployeeDto.toModel(employee));
        return employees;
    }

    public EmployeeAllOrdersDto readAllOrders(Long employee_id) throws CustomerNotExistException {
        if (employeeRepository.findById(employee_id).isEmpty())
            throw new CustomerNotExistException("Employee does not exist!");
        return EmployeeAllOrdersDto.toAllModel(employeeRepository.findById(employee_id).get());
    }
}
