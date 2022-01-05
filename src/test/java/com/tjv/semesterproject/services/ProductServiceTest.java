package com.tjv.semesterproject.services;

import com.tjv.semesterproject.entity.ProductEntity;
import com.tjv.semesterproject.exceptions.ProductNotExistException;
import com.tjv.semesterproject.model.ProductDto;
import com.tjv.semesterproject.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;

    private ProductEntity product;
    private final ProductEntity invalidProduct = new ProductEntity();

    @BeforeEach
    void setUp() {
        product = new ProductEntity("test", (long) 1);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(productRepository.save(invalidProduct)).thenReturn
                (new ProductEntity(null, null));
    }

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(productService).isNotNull();
        assertThat(productRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void registerTest(){
        this.product = productRepository.save(product);
        Mockito.verify(productRepository, Mockito.times(1)).save(this.product);
        assertThat(this.product.getName()).isNotNull();
    }

    @Test
    @Order(3)
    public void registerInvalidTest(){
        var savedEntity = productRepository.save(new ProductEntity());
        Mockito.verify(productRepository, Mockito.times(1)).save(new ProductEntity());
        assertNull(savedEntity.getName());
    }


    @Test
    @Order(4)
    public void updateTest(){
        this.product.setName("updated");
        this.product = productRepository.save(this.product);

        Mockito.verify(productRepository, Mockito.times(1)).save(this.product);

        assertEquals(this.product.getName(), "updated");
    }

    @Test
    @Order(5)
    public void updateInvalidTest() {
        this.product = productRepository.save(this.invalidProduct);

        Mockito.verify(productRepository, Mockito.times(1)).save(this.product);

        assertNull(product.getName());
    }


    @Test
    @Order(6)
    public void getTest(){
        var savedProduct = productRepository.save(this.product);
        var savedDtoProduct = new ProductDto( "test", (long) 1);

        if(savedProduct.equals(product))
            Mockito.when(ProductDto.fromModel(savedProduct)).thenReturn(savedDtoProduct);

        var getResult = ProductDto.fromModel(savedProduct);
        assertEquals(getResult, savedDtoProduct);
    }

    @Test
    @Order(6)
    public void getInvalidTest(){
        var savedProduct = productRepository.save(this.invalidProduct);
        var savedDtoProduct = new ProductDto( "test", (long) 1);

        if(savedProduct.equals(product))
            Mockito.when(ProductDto.fromModel(savedProduct)).thenReturn(savedDtoProduct);

        var getResult = ProductDto.fromModel(savedProduct);
        assertNotEquals(getResult, savedDtoProduct);
    }


    @Test
    @Order(7)
    public void deleteTest() throws ProductNotExistException {
        Mockito.when(productRepository.findById(product.getName())).thenReturn(Optional.of(product));

        productService.deleteProduct(product.getName());
        Mockito.verify(productRepository, Mockito.atLeast(1)).deleteById(product.getName());
    }

    @Test
    @Order(8)
    public void deleteInvalidTest(){
        Assertions.assertThrows(ProductNotExistException.class,
                () -> productService.deleteProduct(product.getName()));
    }

    @Test
    @Order(9)
    public void getAllTest(){
        List<ProductEntity> productList = new ArrayList<>();
        productList.add(this.product);

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        var savedProducts = productService.readAll();
        assertThat(savedProducts.size()).isGreaterThan(0);
    }

    @Test
    @Order(10)
    public void getAllInvalidTest(){
        List<ProductEntity> productList = new ArrayList<>();

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        var savedProducts = productService.readAll();
        assertFalse(savedProducts.size() > 0);
    }
}
