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

    public static EmployeeDto fromModel(EmployeeEntity employee) {
        return new EmployeeDto(employee.getId(), employee.getName(), employee.getSurname(), employee.getSalary());
    }

    public static EmployeeEntity toModel(EmployeeDto employeeDto) {
        return new EmployeeEntity(employeeDto.getName(), employeeDto.getSurname(), employeeDto.getSalary());
    }
}
