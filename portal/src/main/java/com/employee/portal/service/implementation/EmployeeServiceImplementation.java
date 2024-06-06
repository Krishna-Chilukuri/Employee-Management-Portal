package com.employee.portal.service.implementation;

import com.employee.portal.model.Employee;
import com.employee.portal.repository.EmployeeRepository;
import com.employee.portal.service.EmployeeService;
import com.employee.portal.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImplementation implements EmployeeService {

    public EmployeeRepository employeeRepository;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository) {
        super();
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee emp) {
        return employeeRepository.save(emp);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(long empId) {
        Optional<Employee> empOfId = employeeRepository.findById(empId);
        return empOfId.orElseGet(Employee::new);
    }

    @Override
    public Employee updateEmployee(Employee emp, long empId) {
        Employee existEmployee = employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", empId));
        existEmployee.setEmployeeName(emp.getEmployeeName());
        employeeRepository.save(existEmployee);
        return existEmployee;
    }

    @Override
    public void deleteEmployee(long empId) {
        employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Employee", "Id", empId));
        employeeRepository.deleteById(empId);
    }
}