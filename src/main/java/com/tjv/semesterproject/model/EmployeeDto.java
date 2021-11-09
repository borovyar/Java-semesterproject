package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
    private String surname;
    private Long salary;

    public static EmployeeDto toModel(EmployeeEntity employee) {
        return new EmployeeDto(employee.getId(), employee.getName(), employee.getSurname(), employee.getSalary());
    }
}
