package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.OrderEntity;
import com.tjv.semesterproject.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String customer_name;
    private String customer_surname;
    private String employee_name;
    private String employee_surname;
    private ArrayList<String> products;

    public static OrderDto fromModel(OrderEntity order) {
        ArrayList<String> productNameList = new ArrayList<>();
        for (ProductEntity product : order.getProducts())
            productNameList.add(product.getName());
        return new OrderDto(order.getId(), order.getCustomer().getName(),order.getCustomer().getSurname(),
                order.getEmployee().getName(),order.getEmployee().getSurname(), productNameList);
    }

}
