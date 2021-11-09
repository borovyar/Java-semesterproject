package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductOrderDto {
    private String name;
    private Long price;
    private Long order_id;

    public static ProductOrderDto toOrderModel(ProductEntity product, OrderEntity order) {
        return new ProductOrderDto(product.getName(), product.getPrice(), order.getId());
    }
}
