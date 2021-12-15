package com.tjv.clientside.client;

import com.tjv.clientside.model.CustomerDto;
import com.tjv.clientside.uinterface.view.CustomerView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;

@Component
public class CustomerClient {
    private static final String ID_URI = "/{id}";
    private final WebClient customerWebClient;
    private final CustomerView customerView;
    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        try {
            this.customerId = customerId;
            if (customerId != null)
                readById();
        } catch (WebClientException e) {
            this.customerId = null;
            throw e;
        }
    }

    public CustomerClient(@Value("${cafeteria_backend_url}") String backendUrl, CustomerView customerView) {
        customerWebClient = WebClient.create(backendUrl + "/customers");
        this.customerView = customerView;
    }

    public Collection<CustomerDto> readAll() {
        return customerWebClient.get()
                .retrieve()
                .bodyToFlux(CustomerDto.class)
                .collectList()
                .block(Duration.ofSeconds(2));
    }

    public CustomerDto  readById() {
        if (customerId == null)
            throw new IllegalStateException("customer ID does not set");
        return customerWebClient.get()
                .uri(ID_URI, customerId).retrieve()
                .bodyToMono(CustomerDto.class)
                .block();
    }

    public void update(CustomerDto  customerDto) {
        if (customerId == null)
            throw new IllegalStateException("customer ID does not set");
        customerDto.setId(customerId);
        customerWebClient.put()
                .uri(ID_URI, customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerDto)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {
                }, e -> {
                    setCustomerId(null);
                    customerView.printErrorUpdateCustomer(e);
                })
        ;
    }

    public void delete() {
        if (customerId == null)
            throw new IllegalStateException("customer ID does not set");
        customerWebClient.delete()
                .uri(ID_URI, customerId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setCustomerId(null), e -> {
                    setCustomerId(null);
                    customerView.printErrorGeneric(e);
                })
        ;
    }
}
