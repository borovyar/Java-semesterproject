package com.tjv.clientside.uinterface.console;

import com.tjv.clientside.client.CustomerClient;
import com.tjv.clientside.model.CustomerDto;
import com.tjv.clientside.uinterface.view.CustomerView;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

@ShellComponent
public class CustomerConsole {
    private final CustomerClient customerClient;
    private final CustomerView customerView;

    public CustomerConsole(CustomerClient customerClient, CustomerView customerView) {
        this.customerClient = customerClient;
        this.customerView = customerView;
    }

    @ShellMethod("Retrieve details on current customer")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void printCustomer() {
        try {
            var customerDto = customerClient.readById();
            customerView.printCustomer(customerDto);
        } catch (WebClientException e) {
            unsetCustomerId();
            customerView.printErrorCustomer(e);
        }
    }


    @ShellMethod("Retrieve list of all customers")
    public void listAllCustomers() {
        try {
            var customers = customerClient.readAll();
            customerView.printAllCustomers(customers);
        } catch (WebClientException e) {
            customerView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set customer id")
    public void setCustomerId(Long customerId) {
        try {
            customerClient.setCustomerId(customerId);
        } catch (WebClientException e) {
            customerView.printErrorCustomer(e);
        }
    }

    @ShellMethod("Unset current customer id")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void unsetCustomerId() {
        customerClient.setCustomerId(null);
    }

    @ShellMethod("Update a customer")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void updateCustomer(String name, String surname) {
        try {
            customerClient.update(new CustomerDto(name, surname));
        } catch (WebClientException e) {
            customerView.printErrorUpdateCustomer(e);
        }
    }

    @ShellMethod("Delete a customer")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void deleteCustomer() {
        try {
            customerClient.delete();
        } catch (WebClientException e) {
            customerView.printErrorUpdateCustomer(e);
        }
    }

    public Availability currentCustomerNeededAvailability() {
        return customerClient.getCustomerId() == null ?
                Availability.unavailable("Customer's id needs to be set first") : Availability.available();
    }
}
