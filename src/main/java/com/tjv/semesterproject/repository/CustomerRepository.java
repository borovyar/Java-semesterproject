package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
    CustomerEntity findByNameAndSurname(String name, String surname);
}
