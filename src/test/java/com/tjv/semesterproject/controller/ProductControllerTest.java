package com.tjv.semesterproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjv.semesterproject.entity.ProductEntity;
import com.tjv.semesterproject.model.ProductDto;
import com.tjv.semesterproject.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc productMvc;
    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper;
    private ProductEntity product;
    private ProductDto productDto;

    @BeforeEach
    public void setUp(){
        this.product = new ProductEntity("web", 10L);
        this.productDto = ProductDto.fromModel(product);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void addProductTest() throws Exception {
        Mockito.when(productService.registerProduct(productDto)).
                thenReturn(productDto);

        productMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(product.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(product.getPrice().intValue())));
    }

    @Test
    @Order(2)
    public void addProduct400Test() throws Exception {
        productMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    @Order(3)
    public void updateProductTest() throws Exception {
        var updatedProduct = new ProductDto("update", 1L);
        Mockito.when(productService.updateProduct(updatedProduct, product.getName())).
                thenReturn(updatedProduct);

        productMvc.perform(MockMvcRequestBuilders.put("/products/{id}", product.getName())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(updatedProduct.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(updatedProduct.getPrice().intValue())));
    }

    @Test
    @Order(4)
    public void updateProduct400Test() throws Exception {
        productMvc.perform(MockMvcRequestBuilders.put("/products/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("update")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
    public void getProductTest() throws Exception {
        Mockito.when(productService.getProduct(product.getName())).
                thenReturn(productDto);

        productMvc.perform(MockMvcRequestBuilders.get("/products/{id}", product.getName())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(product.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(product.getPrice().intValue())));
    }

    @Test
    @Order(6)
    public void getProduct404Test() throws Exception {
        productMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 2L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @Order(7)
    public void deleteProductTest() throws Exception {
        var response = productMvc.perform(MockMvcRequestBuilders
                        .delete("/products/{id}", product.getName()))
                .andReturn();

        assertEquals(response.getResponse().getStatus(), 200);
        assertEquals(response.getResponse().getContentAsString(),
                product.getName() + " has been deleted");
    }

    @Test
    @Order(8)
    public void deleteProduct404Test() throws Exception {
        productMvc.perform(MockMvcRequestBuilders.delete("/products/{id}", (Object) null))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
