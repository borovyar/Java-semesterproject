package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomerOrderDto {
    private Long order_id;
    private String customer_name;
    private String customer_surname;
    private Set<String> products_name;

    public static CustomerOrderDto toModel(OrderEntity order) {
        return new CustomerOrderDto(order.getId(), order.getCustomer().getName(), order.getCustomer().getSurname(),
                order.getProducts().
                        stream().
                        map(ProductEntity::getName).
                        collect(Collectors.toSet()));
    }
}
