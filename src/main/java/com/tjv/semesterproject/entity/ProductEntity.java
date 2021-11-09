package com.tjv.semesterproject.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @Column(name = "id")
    private String name;
    @NotNull
    @Column(name = "product_price")
    private Long price;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"))
    private List<OrderEntity> orders;

    public void addOrder ( OrderEntity order){ this.orders.add(order); }
    public void removeOrder ( OrderEntity order){this.orders.remove(order);}

}

