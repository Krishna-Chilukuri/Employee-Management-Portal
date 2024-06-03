package com.employee.portal.controller;

import com.employee.portal.model.Employee;
import com.employee.portal.service.EmployeeService;
import com.employee.portal.service.implementation.EmployeeServiceImplementation;

import jakarta.persistence.GeneratedValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin()
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeServiceImplementation employeeServiceImpl;
    public EmployeeController(EmployeeServiceImplementation employeeServiceImpl) {
        super();
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @RequestMapping("/save")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<Employee>(employeeServiceImpl.saveEmployee(employee), HttpStatus.CREATED);
    }

    @RequestMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {
        return employeeServiceImpl.getAllEmployees();
    }
}
