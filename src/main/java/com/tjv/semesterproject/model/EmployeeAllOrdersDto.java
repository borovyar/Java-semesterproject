package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class EmployeeAllOrdersDto {
    private Long id;
    private String name;
    private String surname;
    private Long salary;
    private Set<Long> order_id;

    public static EmployeeAllOrdersDto toAllModel(EmployeeEntity employee) {
        return new EmployeeAllOrdersDto(employee.getId(), employee.getName(), employee.getSurname(),
                employee.getSalary(), employee.getOrders().stream().map(OrderEntity::getId).collect(Collectors.toSet()));
    }
}
