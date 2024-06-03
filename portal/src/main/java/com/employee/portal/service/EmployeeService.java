package com.employee.portal.service;

import com.employee.portal.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee emp);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(long empId);
    Employee updateEmployee(Employee emp, long empId);
    void deleteEmployee(long empId);
}
