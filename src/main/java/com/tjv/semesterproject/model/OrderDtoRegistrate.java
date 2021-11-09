package com.tjv.semesterproject.model;

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
}
