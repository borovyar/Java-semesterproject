package com.tjv.clientside.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
    private String surname;
    private Long salary;

    public EmployeeDto(String name, String surname, Long salary) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.id = null;
    }
}