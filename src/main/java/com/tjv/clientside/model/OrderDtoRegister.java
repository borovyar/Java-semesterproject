package com.tjv.clientside.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoRegister {
    private Long id;
    private String customer_name;
    private String customer_surname;
    private Long employee_id;
    private ArrayList<String> products_id;

    public OrderDtoRegister(String customer_name, String customer_surname, Long employee_id, String[] products_id) {
        this.customer_name = customer_name;
        this.customer_surname = customer_surname;
        this.employee_id = employee_id;
        this.products_id = new ArrayList<>(Arrays.asList(products_id));
    }
}
