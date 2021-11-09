package com.tjv.semesterproject.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "employees" )
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "employee_name")
    private String name;
    @NotNull
    @Column(name = "employee_surname")
    private String surname;
    @NotNull
    @Column(name = "employee_salary")
    private Long salary;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL)
    private List<OrderEntity> orders;

}
