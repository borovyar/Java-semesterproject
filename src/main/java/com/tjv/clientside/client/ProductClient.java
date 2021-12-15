package com.tjv.clientside.client;

import com.tjv.clientside.model.ProductDto;
import com.tjv.clientside.uinterface.view.ProductView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;


@Component
public class ProductClient {
    private static final String ID_URI = "/{id}";
    private final WebClient productWebClient;
    private final ProductView productView;
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        try {
            this.productId = productId;
            if (productId != null)
                readById();
        } catch (WebClientException e) {
            this.productId = null;
            throw e;
        }
    }

    public ProductClient(@Value("${cafeteria_backend_url}") String backendUrl, ProductView productView) {
        productWebClient = WebClient.create(backendUrl + "/products");
        this.productView = productView;
    }


    public ProductDto create(ProductDto productDto) {
        return productWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(productDto)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block(Duration.ofSeconds(2));
    }

    public Collection<ProductDto> readAll() {
        return productWebClient.get()
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .collectList()
                .block(Duration.ofSeconds(5));
    }

    public ProductDto readById() {
        if (productId == null)
            throw new IllegalStateException("Product with this does not set");
        return productWebClient.get()
                .uri(ID_URI, productId).retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }

    public void update(ProductDto productDto) {
        if (productId == null)
            throw new IllegalStateException("Product with this does not set");
        productDto.setName(productId);
        productWebClient.put()
                .uri(ID_URI, productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productDto)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {
                }, e -> {
                    setProductId(null);
                    productView.printErrorUpdateProduct(e);
                })
        ;
    }

    public void delete() {
        if (productId == null)
            throw new IllegalStateException("Product with this name does not set");
        productWebClient.delete()
                .uri(ID_URI, productId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setProductId(null), e -> {
                    setProductId(null);
                    productView.printErrorGeneric(e);
                })
        ;
    }
}
