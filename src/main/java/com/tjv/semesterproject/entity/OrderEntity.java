package com.tjv.semesterproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "orders", cascade = CascadeType.REFRESH)
    private List<ProductEntity> products = new ArrayList<>();

    public OrderEntity(CustomerEntity customer, EmployeeEntity employee) {
        this.customer = customer;
        this.employee = employee;
    }

    public void removeProduct( ProductEntity product ) {
        this.products.remove(product);
    }

    public void setUpdatedProducts(List<ProductEntity> products) {
        for( ProductEntity product : this.products )
            product.removeOrder(this);
        this.products.clear();
        this.products = products;
    }
}
