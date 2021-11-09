package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomerAllOrdersDto {
    private String customer_name;
    private String customer_surname;
    private List<Long> orders_id;

    public static CustomerAllOrdersDto toAllModel(CustomerEntity customer) {
        return new CustomerAllOrdersDto(customer.getName(), customer.getSurname(), customer.getOrders().
                stream().
                map(OrderEntity::getId).
                collect(Collectors.toList()));
    }
}
