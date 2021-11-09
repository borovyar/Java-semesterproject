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

    public static CustomerDto toModel(CustomerEntity customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getSurname());
    }

}
