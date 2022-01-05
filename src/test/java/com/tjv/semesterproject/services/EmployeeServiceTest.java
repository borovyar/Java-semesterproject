package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.exceptions.EmployeeNotExistException;
import com.tjv.semesterproject.model.EmployeeDto;
import com.tjv.semesterproject.repository.EmployeeRepository;
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

@SpringBootTest
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;
    @MockBean
    private EmployeeRepository employeeRepository;

    private EmployeeEntity employee;
    private final EmployeeEntity invalidEmployee = new EmployeeEntity();

    @BeforeEach
    void setUp() {
        employee = new EmployeeEntity("test", "test", (long) 1);
        employee.setId((long) 1);
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);
        Mockito.when(employeeRepository.save(invalidEmployee)).thenReturn
                (new EmployeeEntity(null, null, null));
    }

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(employeeService).isNotNull();
        assertThat(employeeRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void registerTest(){
        this.employee = employeeRepository.save(employee);
        Mockito.verify(employeeRepository, Mockito.times(1)).save(this.employee);
        assertThat(this.employee.getName()).isNotNull();
    }

    @Test
    @Order(3)
    public void registerInvalidTest(){
        var savedEntity = employeeRepository.save(new EmployeeEntity());
        Mockito.verify(employeeRepository, Mockito.times(1)).save(new EmployeeEntity());
        assertNull(savedEntity.getName());
    }


    @Test
    @Order(4)
    public void updateTest(){
        this.employee.setName("updated");
        this.employee = employeeRepository.save(this.employee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(this.employee);

        assertEquals(this.employee.getName(), "updated");
    }

    @Test
    @Order(5)
    public void updateInvalidTest() {
        this.employee = employeeRepository.save(this.invalidEmployee);

        Mockito.verify(employeeRepository, Mockito.times(1)).save(this.employee);

        assertNull(employee.getName());
    }


    @Test
    @Order(6)
    public void getTest(){
        var savedEmployee = employeeRepository.save(this.employee);
        var savedDtoEmployee = new EmployeeDto((long) 1, "test", "test", (long) 1);

        if(savedEmployee.equals(employee))
            Mockito.when(EmployeeDto.fromModel(savedEmployee)).thenReturn(savedDtoEmployee);

        var getResult = EmployeeDto.fromModel(savedEmployee);
        assertEquals(getResult, savedDtoEmployee);
    }

    @Test
    @Order(6)
    public void getInvalidTest(){
        var savedEmployee = employeeRepository.save(this.invalidEmployee);
        var savedDtoEmployee = new EmployeeDto(null, "test", "test", (long) 1);

        if(savedEmployee.equals(employee))
            Mockito.when(EmployeeDto.fromModel(savedEmployee)).thenReturn(savedDtoEmployee);

        var getResult = EmployeeDto.fromModel(savedEmployee);
        assertNotEquals(getResult, savedDtoEmployee);
    }


    @Test
    @Order(7)
    public void deleteTest() throws EmployeeNotExistException {
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(employee.getId());
        Mockito.verify(employeeRepository, Mockito.atLeast(1)).deleteById(employee.getId());
    }

    @Test
    @Order(8)
    public void deleteInvalidTest(){
        Assertions.assertThrows(EmployeeNotExistException.class,
                () -> employeeService.deleteEmployee(employee.getId()));
    }

    @Test
    @Order(9)
    public void getAllTest(){
        List<EmployeeEntity> entityList = new ArrayList<>();
        entityList.add(this.employee);

        Mockito.when(employeeRepository.findAll()).thenReturn(entityList);

        var savedEmployees = employeeService.readAll();
        assertThat(savedEmployees.size()).isGreaterThan(0);
    }

    @Test
    @Order(10)
    public void getAllInvalidTest(){
        List<EmployeeEntity> entityList = new ArrayList<>();

        Mockito.when(employeeRepository.findAll()).thenReturn(entityList);

        var savedEmployees = employeeService.readAll();
        assertFalse(savedEmployees.size() > 0);
    }
}
