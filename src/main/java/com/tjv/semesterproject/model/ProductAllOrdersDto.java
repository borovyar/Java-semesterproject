package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ProductAllOrdersDto {
    private String product_name;
    private Long price;
    private Set<Long> orders_id;

    public static ProductAllOrdersDto toModel (ProductEntity product){
        return new ProductAllOrdersDto(product.getName(), product.getPrice(), product.getOrders().
                stream().map(OrderEntity::getId).
                collect(Collectors.toSet()));
    }
}
