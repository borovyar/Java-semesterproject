package com.tjv.clientside.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String surname;

    public CustomerDto(String name, String surname) {
        this.id = null;
        this.name = name;
        this.surname = surname;
    }
}
