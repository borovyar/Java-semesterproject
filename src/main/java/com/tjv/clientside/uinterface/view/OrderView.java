package com.tjv.clientside.uinterface.view;


import com.tjv.clientside.model.OrderDto;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class OrderView {
    private void printProducts(ArrayList<String> productDtos){
        for(String product : productDtos)
            System.out.println("       Product name : " + product);
    }

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

    public void printOrder(OrderDto orderDto) {
        System.out.println("Order id: " + orderDto.getId());
        System.out.println("    Customer name | surname : " + orderDto.getCustomer_name() +
                " | " + orderDto.getCustomer_surname());
        System.out.println("   Employee name | surname : " + orderDto.getEmployee_name() + " | " +
                orderDto.getEmployee_surname());
        printProducts(orderDto.getProducts());
    }

    public void printErrorOrder(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Order with given id does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllOrders(Collection<OrderDto> orders) {
        orders.forEach(u -> System.out.println("id: " + u.getId()));
    }

    public void printErrorUpdateOrder(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.
                    println(AnsiOutput.toString(AnsiColor.YELLOW, "Order does not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }
}
