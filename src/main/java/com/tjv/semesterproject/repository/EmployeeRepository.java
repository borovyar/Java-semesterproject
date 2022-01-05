package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository< EmployeeEntity, Long > {
    EmployeeEntity findByNameAndSurname( String name, String surname );
    EmployeeEntity findByName( String name );
    EmployeeEntity findBySalary( Long salary );
}
