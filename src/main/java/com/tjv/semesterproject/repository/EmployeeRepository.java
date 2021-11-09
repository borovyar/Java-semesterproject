package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository< EmployeeEntity, Long > {
    public EmployeeEntity findByNameAndSurname( String name, String surname );
    public EmployeeEntity findBySalary( Long salary );
}
