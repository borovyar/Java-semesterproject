package com.tjv.clientside.client;

import com.tjv.clientside.model.CustomerDto;
import com.tjv.clientside.model.OrderDto;
import com.tjv.clientside.model.OrderDtoRegister;
import com.tjv.clientside.uinterface.view.CustomerView;
import com.tjv.clientside.uinterface.view.OrderView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;

@Component
public class OrderClient {
    private static final String ID_URI = "/{id}";
    private final WebClient orderWebClient;
    private final OrderView orderView;
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        try {
            this.orderId = orderId;
            if (orderId != null)
                readById();
        } catch (WebClientException e) {
            this.orderId = null;
            throw e;
        }
    }

    public OrderClient(@Value("${cafeteria_backend_url}") String backendUrl, OrderView orderView) {
        orderWebClient = WebClient.create(backendUrl + "/orders");
        this.orderView = orderView;
    }


    public Collection<OrderDto> readAll() {
        return orderWebClient.get()
                .retrieve()
                .bodyToFlux(OrderDto.class)
                .collectList()
                .block(Duration.ofSeconds(2));
    }

    public OrderDto readById() {
        if (orderId == null)
            throw new IllegalStateException("order ID does not set");
        return orderWebClient.get()
                .uri(ID_URI, orderId).retrieve()
                .bodyToMono(OrderDto.class)
                .block();
    }

    public void update(OrderDtoRegister  orderDtoRegister) {
        if (orderId == null)
            throw new IllegalStateException("order ID does not set");
        orderDtoRegister.setId(orderId);
        orderWebClient.put()
                .uri(ID_URI, orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderDtoRegister)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {
                }, e -> {
                    setOrderId(null);
                    orderView.printErrorUpdateOrder(e);
                })
        ;
    }

    public void delete() {
        if (orderId == null)
            throw new IllegalStateException("order ID does not set");
        orderWebClient.delete()
                .uri(ID_URI, orderId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setOrderId(null), e -> {
                    setOrderId(null);
                    orderView.printErrorGeneric(e);
                })
        ;
    }
}
