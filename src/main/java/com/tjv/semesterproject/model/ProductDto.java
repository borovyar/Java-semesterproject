package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {
    private String name;
    private Long price;

    public static ProductDto fromModel(ProductEntity product) {
        return new ProductDto(product.getName(), product.getPrice());
    }

    public static ProductEntity toModel(ProductDto productDto) {
        return new ProductEntity(productDto.getName(), productDto.getPrice());
    }
}