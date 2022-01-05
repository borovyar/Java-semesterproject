package com.tjv.semesterproject.controller;

import com.tjv.semesterproject.exceptions.*;
import com.tjv.semesterproject.model.OrderDtoRegistrate;
import com.tjv.semesterproject.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity registerOrder(@RequestBody OrderDtoRegistrate orderDtoRegistrate) {
        try {
            return ResponseEntity.ok(orderService.registerOrder(orderDtoRegistrate));
        } catch (ProductNotExistException | EmployeeNotExistException | CustomerExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Product Posting Error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrder(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        } catch (OrderNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order Getting Error");
        }
    }

    @GetMapping
    public ResponseEntity getAllOrders(){
        try {
            return ResponseEntity.ok(orderService.readAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order Getting Error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateOrder(@RequestBody OrderDtoRegistrate orderDto, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(orderDto, id));
        } catch (OrderNotExistException | CustomerNotExistException | EmployeeNotExistException | ProductNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order Putting Error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("order with id " + id + " has been deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Order Deleting Error");
        }
    }
}
