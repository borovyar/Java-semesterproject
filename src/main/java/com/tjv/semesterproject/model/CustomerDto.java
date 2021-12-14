package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;

    public CustomerDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public static CustomerDto fromModel(CustomerEntity customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getSurname());
    }

    public static CustomerEntity toModel(CustomerDto customerDto) {
        return new CustomerEntity(customerDto.getName(), customerDto.getSurname());
    }

}
