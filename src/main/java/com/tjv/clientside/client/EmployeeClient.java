package com.tjv.clientside.client;

import com.tjv.clientside.model.EmployeeDto;
import com.tjv.clientside.model.OrderDto;
import com.tjv.clientside.model.OrderDtoRegister;
import com.tjv.clientside.uinterface.view.EmployeeView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;


@Component
public class EmployeeClient {
    private static final String ID_URI = "/{id}";
    private final WebClient employeeWebClient;
    private final WebClient orderWebClient;
    private final EmployeeView employeeView;
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        try {
            this.employeeId = employeeId;
            if (employeeId != null)
                readById();
        } catch (WebClientException e) {
            this.employeeId = null;
            throw e;
        }
    }

    public EmployeeClient(@Value("${cafeteria_backend_url}") String backendUrl, EmployeeView employeeView) {
        employeeWebClient = WebClient.create(backendUrl + "/employees");
        orderWebClient = WebClient.create(backendUrl + "/orders");
        this.employeeView = employeeView;
    }


    public EmployeeDto create(EmployeeDto employeeDto) {
        return employeeWebClient.post() // HTTP POST
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)// set HTTP header
                .bodyValue(employeeDto) // POST data
                .retrieve() // request specification finished
                .bodyToMono(EmployeeDto.class) // interpret response body as one element
                .block(Duration.ofSeconds(2)); // call synchronously with timeout
    }

    public OrderDto registerOrder (String name, String surname, String[] products){
        return orderWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new OrderDtoRegister(name, surname, this.employeeId, products))
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block(Duration.ofSeconds(2));
    }


    public Collection<EmployeeDto> readAll() {
        return employeeWebClient.get() // HTTP GET
                .retrieve() // request specification finished
                .bodyToFlux(EmployeeDto.class) // interpret response body as a collection
                .collectList() // collect all elements as list
                .block(Duration.ofSeconds(5)); // call synchronously with timeout
    }

    public EmployeeDto readById() {
        if (employeeId == null)
            throw new IllegalStateException("ID does not set");
        return employeeWebClient.get()
                .uri(ID_URI, employeeId).retrieve()
                .bodyToMono(EmployeeDto.class)
                .block();
    }

    public void update(EmployeeDto employeeDto) {
        if (employeeId == null)
            throw new IllegalStateException("ID does not set");
        employeeDto.setId(employeeId);
        employeeWebClient.put() // HTTP PUT
                .uri(ID_URI, employeeId) // URI /{id}
                .contentType(MediaType.APPLICATION_JSON) // HTTP header
                .bodyValue(employeeDto) // HTTP body
                .retrieve() // request specification finished
                .toBodilessEntity() // do not extract HTTP response body
                .subscribe(x -> {
                }, e -> {
                    setEmployeeId(null);
                    employeeView.printErrorUpdate(e);
                }) // register callbacks: for success and for error
        ;
    }

    public void delete() {
        if (employeeId == null)
            throw new IllegalStateException("ID does not set");
        employeeWebClient.delete() // HTTP DELETE
                .uri(ID_URI, employeeId) // URI
                .retrieve() // request specification finished
                .toBodilessEntity() // do not extract HTTP response body
                .subscribe(x -> setEmployeeId(null), e -> {
                    setEmployeeId(null);
                    employeeView.printErrorGeneric(e);
                }) // register callbacks: for success and for error
        ;
    }
}
