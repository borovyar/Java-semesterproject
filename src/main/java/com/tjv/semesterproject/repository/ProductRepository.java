package com.tjv.semesterproject.repository;

import com.tjv.semesterproject.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository< ProductEntity, String > {
    public ProductEntity findByPrice( Long price );
}
