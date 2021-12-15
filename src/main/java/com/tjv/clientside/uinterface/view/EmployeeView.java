package com.tjv.clientside.uinterface.view;

import com.tjv.clientside.model.EmployeeDto;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;

@Component
public class EmployeeView {
    public void printErrorGeneric(Throwable e) {
        if (e instanceof WebClientRequestException wcre) {
            System.err.println("Error - connection to Cafeteria API. Current uri " + wcre.getUri());
            throw new ExitRequest();
        }
        else if (e instanceof WebClientResponseException.InternalServerError)
            System.err.println("Server error");
        else
            System.err.println(e.getMessage());
    }

    public void printEmployee(EmployeeDto employeeDto) {
        System.out.println("Employee id: " + employeeDto.getId());
        System.out.println("    name: " + employeeDto.getName());
        System.out.println("    surname: " + employeeDto.getSurname());
        System.out.println("    salary: " + employeeDto.getSalary());
    }

    public void printErrorEmployee(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Employee with given id does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllEmployees(Collection<EmployeeDto> employees) {
        employees.forEach(u -> System.out.println("id: " + u.getId() + " name: " + u.getName()));
    }

    public void printErrorUpdate(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.
                    println(AnsiOutput.toString(AnsiColor.YELLOW, "Employee does not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }
}
