package com.tjv.clientside.uinterface.view;

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
public class ProductView {
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

    public void printProduct(ProductDto productDto) {
        System.out.println("Product name: " + productDto.getName());
        System.out.println("    price: " + productDto.getPrice());
    }

    public void printErrorProduct(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Product with given name does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllProducts(Collection<ProductDto> products) {
        products.forEach(u -> System.out.println("name: " + u.getName() + " price: " + u.getPrice()));
    }

    public void printErrorUpdateProduct(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.
                    println(AnsiOutput.toString(AnsiColor.YELLOW, "Product does not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }
}
