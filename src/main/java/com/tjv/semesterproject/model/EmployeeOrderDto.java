package com.tjv.semesterproject.model;

import com.tjv.semesterproject.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeOrderDto {
    private Long id;
    private String name;
    private String surname;
    private Long salary;
    private CustomerOrderDto customerOrderDto;

    public static EmployeeOrderDto toOrderModel(OrderEntity order) {
        return new EmployeeOrderDto(order.getEmployee().getId(), order.getEmployee().getName(),
                                    order.getEmployee().getSurname(), order.getEmployee().getSalary(),
                                    CustomerOrderDto.toModel( order ));
    }
}
