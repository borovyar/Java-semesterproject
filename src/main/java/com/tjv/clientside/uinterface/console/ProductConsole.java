package com.tjv.clientside.uinterface.console;

import com.tjv.clientside.client.ProductClient;
import com.tjv.clientside.model.ProductDto;
import com.tjv.clientside.uinterface.view.ProductView;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

@ShellComponent
public class ProductConsole {
    private final ProductClient productClient;
    private final ProductView productView;

    public ProductConsole(ProductClient productClient, ProductView productView) {
        this.productClient = productClient;
        this.productView = productView;
    }

    @ShellMethod("Register new product")
    public void createProduct(String name, Long price) {
        try {
            productView.printProduct(productClient.create(new ProductDto(name, price)));
        } catch (WebClientException e) {
            System.out.println(e.getMessage());
        }
    }

    @ShellMethod("Retrieve details on current product")
    @ShellMethodAvailability("currentProductNeededAvailability")
    public void printProduct() {
        try {
            var employeeDto = productClient.readById();
            productView.printProduct(employeeDto);
        } catch (WebClientException e) {
            unsetProductName();
            productView.printErrorProduct(e);
        }
    }


    @ShellMethod("Retrieve list of all products")
    public void listAllProducts() {
        try {
            var products = productClient.readAll();
            productView.printAllProducts(products);
        } catch (WebClientException e) {
            productView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set product name")
    public void setProductName(String name) {
        try {
            productClient.setProductId(name);
        } catch (WebClientException e) {
            productView.printErrorProduct(e);
        }
    }

    @ShellMethod("Unset current product name")
    @ShellMethodAvailability("currentProductNeededAvailability")
    public void unsetProductName() {
        productClient.setProductId(null);
    }

    public Availability currentProductNeededAvailability() {
        return productClient.getProductId() == null ?
                Availability.unavailable("Product name needs to be set first") : Availability.available();
    }

    @ShellMethod("Update a product")
    @ShellMethodAvailability("currentProductNeededAvailability")
    public void updateProduct(String name, Long price) {
        try {
            var productDto = new ProductDto(name, price);
            productClient.update(productDto);
        } catch (WebClientException e) {
            productView.printErrorUpdateProduct(e);
        }
    }

    @ShellMethod("Delete a product")
    @ShellMethodAvailability("currentProductNeededAvailability")
    public void deleteProduct() {
        try {
            productClient.delete();
        } catch (WebClientException e) {
            productView.printErrorUpdateProduct(e);
        }
    }
}
