package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.CustomerEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class OrderDtoRegistrate {
    private String customer_name;
    private String customer_surname;
    private Long employee_id;
    private ArrayList<String> products_id;

    public static CustomerDto customerDto(OrderDtoRegistrate orderDtoRegistrate){
        return new CustomerDto(orderDtoRegistrate.customer_name, orderDtoRegistrate.customer_surname);
    }
}
