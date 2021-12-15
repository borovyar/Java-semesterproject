package com.tjv.clientside.uinterface.view;

import com.tjv.clientside.model.CustomerDto;
import com.tjv.clientside.model.ProductDto;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;

@Component
public class CustomerView {
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

    public void printCustomer(CustomerDto customerDto) {
        System.out.println("Customer id: " + customerDto.getId());
        System.out.println("    name: " + customerDto.getName());
        System.out.println("    surname: " + customerDto.getSurname());
    }

    public void printErrorCustomer(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Customer with given id does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllCustomers(Collection<CustomerDto> customers) {
        customers.forEach(u -> System.out.println("id: " + u.getId() + " name: " + u.getName()));
    }

    public void printErrorUpdateCustomer(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.
                    println(AnsiOutput.toString(AnsiColor.YELLOW, "Customer not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }
}
