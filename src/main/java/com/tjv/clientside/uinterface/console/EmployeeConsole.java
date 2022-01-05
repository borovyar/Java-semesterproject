package com.tjv.clientside.uinterface.console;

import com.tjv.clientside.client.EmployeeClient;
import com.tjv.clientside.model.EmployeeDto;
import com.tjv.clientside.uinterface.view.EmployeeView;
import com.tjv.clientside.uinterface.view.OrderView;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.format.DateTimeParseException;

@ShellComponent
public class EmployeeConsole {
    private final EmployeeClient employeeClient;
    private final EmployeeView employeeView;
    private final OrderView orderView;

    public EmployeeConsole(EmployeeClient employeeClient, EmployeeView employeeView, OrderView orderView) {
        this.employeeClient = employeeClient;
        this.employeeView = employeeView;
        this.orderView = orderView;
    }

    @ShellMethod("Retrieve list of all employees")
    public void listAllEmployees() {
        try {
            var employees = employeeClient.readAll();
            employeeView.printAllEmployees(employees);
        } catch (WebClientException e) {
            employeeView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Register new employee")
    public void createEmployee(String name, String surname, Long salary) {
        try {
            employeeView.printEmployee(employeeClient.create(new EmployeeDto(name, surname, salary)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod("Register new order")
    @ShellMethodAvailability("currentEmployeeNeededAvailability")
    public void createOrder(String name, String surname, String[] products) {
        try {
            orderView.printOrder(employeeClient.registerOrder( name, surname, products ));
        } catch (WebClientException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod("Set employee id")
    public void setEmployeeId(Long employeeId) {
        try {
            employeeClient.setEmployeeId(employeeId);
        } catch (WebClientException e) {
            employeeView.printErrorEmployee(e);
        }
    }

    @ShellMethod("Unset current employee id (go to scope of employees)")
    @ShellMethodAvailability("currentEmployeeNeededAvailability")
    public void unsetEmployeeId() {
        employeeClient.setEmployeeId(null);
    }

    public Availability currentEmployeeNeededAvailability() {
        return employeeClient.getEmployeeId() == null ?
                Availability.unavailable("Employee's id needs to be set first") : Availability.available();
    }

    @ShellMethod("Retrieve details on current employee")
    @ShellMethodAvailability("currentEmployeeNeededAvailability")
    public void printEmployee() {
        try {
            var employeeDto = employeeClient.readById();
            employeeView.printEmployee(employeeDto);
        } catch (WebClientException e) {
            unsetEmployeeId();
            employeeView.printErrorEmployee(e);
        }
    }

    @ShellMethod("Update an employee")
    @ShellMethodAvailability("currentEmployeeNeededAvailability")
    public void updateEmployee(String name, String surname, Long salary) {
        try {
            var employeeDto = new EmployeeDto(name, surname, salary);
            employeeClient.update(employeeDto);
        } catch (WebClientException | DateTimeParseException e) {
            employeeView.printErrorUpdate(e);
        }
    }

    @ShellMethod("Delete an employee")
    @ShellMethodAvailability("currentEmployeeNeededAvailability")
    public void deleteUser() {
        try {
            employeeClient.delete();
        } catch (WebClientException e) {
            employeeView.printErrorUpdate(e);
        }
    }
}
