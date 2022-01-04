package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.ProductEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    private ProductEntity productEntity;

    @Test
    @Order(1)
    public void injectionTest(){
        assertThat(productRepository).isNotNull();
    }
    @Test
    @Order(2)
    public void postTest(){
        this.productEntity = productRepository.save(new ProductEntity("post", (long) 1));
        assertThat(productEntity).isNotNull();
        assertEquals(productRepository.findById(productEntity.getName()).get().getName(), "post");
    }

    @Test
    @Order(3)
    public void updateTest(){
        this.productEntity = productRepository.save(new ProductEntity("update", (long) 1));
        assertThat(productEntity).isNotNull();
        productEntity.setPrice((long) 11);
        assertEquals(productRepository.save(productEntity).getName(), "update");
    }

    @Test
    @Order(4)
    public void getTest(){
        this.productEntity = productRepository.save(new ProductEntity("get", (long) 1));
        assertThat(productEntity).isNotNull();
        assertEquals(productRepository.findById(productEntity.getName()).get().getName(), "get");
    }

    @Test
    @Order(5)
    public void deleteTest(){
        this.productEntity = productRepository.save(new ProductEntity("delete", (long) 1));
        assertThat(productEntity).isNotNull();
        productRepository.deleteById(productEntity.getName());
        assertTrue(productRepository.findById(productEntity.getName()).isEmpty());
    }
}
