package com.tjv.clientside.uinterface.console;

import com.tjv.clientside.client.OrderClient;
import com.tjv.clientside.model.OrderDtoRegister;
import com.tjv.clientside.uinterface.view.OrderView;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;


@ShellComponent
public class OrderConsole {
    private final OrderClient orderClient;
    private final OrderView orderView;

    public OrderConsole(OrderClient orderClient, OrderView orderView) {
        this.orderClient = orderClient;
        this.orderView = orderView;
    }

    @ShellMethod("Retrieve details on current order")
    @ShellMethodAvailability("currentOrderNeededAvailability")
    public void printOrder() {
        try {
            orderView.printOrder(orderClient.readById());
        } catch (WebClientException e) {
            unsetOrderId();
            orderView.printErrorOrder(e);
        }
    }


    @ShellMethod("Retrieve list of all orders")
    public void listAllOrders() {
        try {
            var orders = orderClient.readAll();
            orderView.printAllOrders(orders);
        } catch (WebClientException e) {
            orderView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set order id")
    public void setOrderId(Long orderId) {
        try {
            orderClient.setOrderId(orderId);
        } catch (WebClientException e) {
            orderView.printErrorOrder(e);
        }
    }

    @ShellMethod("Unset current order id")
    @ShellMethodAvailability("currentOrderNeededAvailability")
    public void unsetOrderId() {
        orderClient.setOrderId(null);
    }

    @ShellMethod("Update an order")
    @ShellMethodAvailability("currentOrderNeededAvailability")
    public void updateOrder(String name, String surname, Long employeeId, String[] products) {
        try {
            orderClient.update(new OrderDtoRegister(name, surname, employeeId, products));
        } catch (WebClientException e) {
            orderView.printErrorUpdateOrder(e);
        }
    }

    @ShellMethod("Delete an order")
    @ShellMethodAvailability("currentOrderNeededAvailability")
    public void deleteOrder() {
        try {
            orderClient.delete();
        } catch (WebClientException e) {
            orderView.printErrorUpdateOrder(e);
        }
    }

    public Availability currentOrderNeededAvailability() {
        return orderClient.getOrderId() == null ?
                Availability.unavailable("Order's id needs to be set first") : Availability.available();
    }
}
