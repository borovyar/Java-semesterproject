package com.tjv.semesterproject.controller;

import com.tjv.semesterproject.entity.EmployeeEntity;
import com.tjv.semesterproject.exceptions.EmployeeExistException;
import com.tjv.semesterproject.exceptions.EmployeeNotExistException;
import com.tjv.semesterproject.exceptions.OrderNotExistException;
import com.tjv.semesterproject.model.EmployeeDto;
import com.tjv.semesterproject.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity registerEmployee(@RequestBody EmployeeDto employee) {
        try {
            return ResponseEntity.ok(employeeService.registerEmployee(employee));
        } catch (EmployeeExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Posting Error");
        }
    }

    @GetMapping("/")
    public ResponseEntity getEmployee(@RequestParam(value = "id") Long id) {
        try {
            return ResponseEntity.ok(employeeService.getEmployee(id));
        } catch (EmployeeNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Getting Error");
        }
    }

    @GetMapping("/{employee_id}/orders/")
    public ResponseEntity getEmployeeOrder(@PathVariable("employee_id") Long employee_id,
                                      @RequestParam(value = "order_id") Integer order_id) {
        try {
            return ResponseEntity.ok(employeeService.getEmployeeOrder(employee_id, order_id));
        } catch (OrderNotExistException | EmployeeNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Getting Order Error");
        }
    }

    @GetMapping
    public ResponseEntity getAllEmployees() {
        try {
            return ResponseEntity.ok(employeeService.readAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Getting Error");
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity getAllOrders(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(employeeService.readAllOrders(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Getting Orders Error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable("id") Long id) {
        try {
            employeeService.updateEmployee(employeeDto, id);
            return ResponseEntity.ok("Employee has been updated");
        } catch (EmployeeNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Putting Error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee with id:" + id + " has been deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee Deleting Error");
        }
    }

}
