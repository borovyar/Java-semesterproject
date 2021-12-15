package com.tjv.clientside.model;

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
    private ArrayList<String> products = new ArrayList<>();
}
