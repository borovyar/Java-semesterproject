package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;


public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
