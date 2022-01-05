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
        return employeeWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDto)
                .retrieve()
                .bodyToMono(EmployeeDto.class)
                .block(Duration.ofSeconds(2));
    }

    public OrderDto registerOrder (String name, String surname, String[] products){
        return orderWebClient.post().contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .bodyValue(new OrderDtoRegister(name, surname, this.employeeId, products))
                .retrieve()
                .bodyToMono(OrderDto.class)
                .block(Duration.ofSeconds(2));
    }


    public Collection<EmployeeDto> readAll() {
        return employeeWebClient.get()
                .retrieve()
                .bodyToFlux(EmployeeDto.class)
                .collectList()
                .block(Duration.ofSeconds(5));
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
        employeeWebClient.put()
                .uri(ID_URI, employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeDto)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {
                }, e -> {
                    setEmployeeId(null);
                    employeeView.printErrorUpdate(e);
                })
        ;
    }

    public void delete() {
        if (employeeId == null)
            throw new IllegalStateException("ID does not set");
        employeeWebClient.delete()
                .uri(ID_URI, employeeId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setEmployeeId(null), e -> {
                    setEmployeeId(null);
                    employeeView.printErrorGeneric(e);
                })
        ;
    }
}
